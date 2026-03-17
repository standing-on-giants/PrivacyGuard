package com.example.DataModellingProject.privacy.aspect;

import com.example.DataModellingProject.privacy.model.TableRule;
import com.example.DataModellingProject.privacy.service.ArxService;
import com.example.DataModellingProject.privacy.service.PrivacyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@Aspect
@Component
public class PrivacyAspect {

    private final PrivacyService privacyService;
    private final ArxService arxService;

    public PrivacyAspect(PrivacyService privacyService, ArxService arxService) {
        this.privacyService = privacyService;
        this.arxService = arxService;
    }

    @Around("execution(* com.example.DataModellingProject.controller.*.*(..))")
    public Object applyPrivacyRules(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. Get the result from the controller
        Object result = joinPoint.proceed();

        // 2. Check authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return result;
        }

        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("")
                .replace("ROLE_", "")
                .toLowerCase();

        // 3. Extract the actual payload, whether it's wrapped in a ResponseEntity or not!
        Object bodyToMask = null;
        if (result instanceof ResponseEntity) {
            bodyToMask = ((ResponseEntity<?>) result).getBody();
        } else {
            bodyToMask = result; // It's just a raw object or list
        }

        // 4. Apply the Privacy Rules to the payload
        if (bodyToMask != null) {
            String tableName = getTableNameFromBody(bodyToMask);

            if (tableName != null) {
                TableRule rule = privacyService.getTableRule(role, tableName);

                if (rule != null) {
                    if (bodyToMask instanceof Collection) {
                        Collection<?> collectionBody = (Collection<?>) bodyToMask;

                        try {
                            arxService.applyArxAnonymization(collectionBody, rule);
                        } catch (Exception e) {
                            System.err.println("ARX Anonymization failed or skipped: " + e.getMessage());
                        }

                        for (Object item : collectionBody) {
                            maskObject(item, rule);
                        }
                    } else {
                        maskObject(bodyToMask, rule);
                    }
                }
            }
        }

        // 5. Return the result. Because Java passes objects by reference,
        // modifying 'bodyToMask' automatically modifies the data inside 'result'!
        return result;
    }

    /**
     * Determines the class name (Table Name) of the object we are returning.
     * If it's a List, it peeks at the first item to get its class.
     */
    private String getTableNameFromBody(Object body) {
        Object itemToCheck = body;

        if (body instanceof Collection) {
            Collection<?> collection = (Collection<?>) body;
            if (collection.isEmpty()) {
                return null;
            }
            itemToCheck = collection.iterator().next();
        }

        // 1. Check if the class has our custom @PrivacyTable annotation
        com.example.DataModellingProject.privacy.annotation.PrivacyTable annotation =
                itemToCheck.getClass().getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyTable.class);

        if (annotation != null) {
            // If it has the annotation, use the name provided in it (e.g., "Appointment")
            return annotation.value();
        }

        // 2. If no annotation is found, fall back to the raw class name (e.g., "Patient")
        return itemToCheck.getClass().getSimpleName();
    }

    /**
     * Applies simple column masking (e.g. changing strings to "***")
     * based on the TableRule passed in.
     */
    private void maskObject(Object item, TableRule rule) {
        if (item == null || rule.getMaskColumns().isEmpty()) return;

        List<String> columnsToMask = rule.getMaskColumns();
        Field[] fields = item.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (columnsToMask.stream().anyMatch(c -> c.equalsIgnoreCase(field.getName()))) {
                try {
                    field.setAccessible(true);
                    if (field.getType().equals(String.class)) {
                        field.set(item, "***RESTRICTED***");
                    } else {
                        field.set(item, null);
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Could not mask field: " + field.getName());
                }
            }
        }
    }
}