package com.example.DataModellingProject.privacy.aspect;

import com.example.DataModellingProject.context.UserContext;
import com.example.DataModellingProject.privacy.model.TableRule;
import com.example.DataModellingProject.privacy.service.ArxService;
import com.example.DataModellingProject.privacy.service.PrivacyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Aspect
@Component
public class PrivacyAspect {

    private final PrivacyService privacyService;
    private final ArxService arxService;
    private final UserContext userContext; // 1. Inject the new context

    public PrivacyAspect(PrivacyService privacyService, ArxService arxService, UserContext userContext) {
        this.privacyService = privacyService;
        this.arxService = arxService;
        this.userContext = userContext;
    }

    @Around("execution(* com.example.DataModellingProject.controller.*.*(..))")
    public Object applyPrivacyRules(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. Get the result from the controller
        Object result = joinPoint.proceed();

        // 2. Check authentication using the Context (No more SecurityContextHolder!)
        if (!userContext.isAuthenticated()) {
            return result;
        }

        // 3. Get the pre-processed role directly from the Context
        String role = userContext.getRole();

        // 4. Extract the actual payload
        Object bodyToMask = null;
        if (result instanceof ResponseEntity) {
            bodyToMask = ((ResponseEntity<?>) result).getBody();
        } else {
            bodyToMask = result;
        }

        // 5. Apply the Privacy Rules to the payload
        if (bodyToMask != null) {
            String primaryTableName = getTableNameFromBody(bodyToMask);

            if (primaryTableName != null) {

                if (bodyToMask instanceof Collection) {
                    Collection<?> collectionBody = (Collection<?>) bodyToMask;

                    // Apply ARX using the cross-table resolver
                    try {
                        arxService.applyArxAnonymization(collectionBody, role, primaryTableName);
                    } catch (Exception e) {
                        System.err.println("ARX Anonymization failed or skipped: " + e.getMessage());
                    }

                    // Mask items, passing the role so it can fetch cross-table rules
                    for (Object item : collectionBody) {
                        maskObject(item, role);
                    }
                } else {
                    maskObject(bodyToMask, role);
                }
            }
        }

        // 6. Return the result
        return result;
    }

    private String getTableNameFromBody(Object body) {
        Object itemToCheck = body;

        if (body instanceof Collection) {
            Collection<?> collection = (Collection<?>) body;
            if (collection.isEmpty()) {
                return null;
            }
            itemToCheck = collection.iterator().next();
        }

        com.example.DataModellingProject.privacy.annotation.PrivacyTable annotation =
                itemToCheck.getClass().getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyTable.class);

        if (annotation != null) {
            return annotation.value();
        }

        return itemToCheck.getClass().getSimpleName();
    }

    /**
     * Applies simple column masking based on the TableRule.
     * Inherits table name from @PrivacyTable if @PrivacyField table is empty.
     */
    private void maskObject(Object item, String role) {
        if (item == null) return;

        // 1. Get the default table name from the top of the class
        String defaultTableName = item.getClass().getSimpleName();
        com.example.DataModellingProject.privacy.annotation.PrivacyTable classAnnotation =
                item.getClass().getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyTable.class);

        if (classAnnotation != null) {
            defaultTableName = classAnnotation.value();
        }

        Field[] fields = item.getClass().getDeclaredFields();

        for (Field field : fields) {
            // 2. Look for the @PrivacyField annotation
            com.example.DataModellingProject.privacy.annotation.PrivacyField privacyField =
                    field.getAnnotation(com.example.DataModellingProject.privacy.annotation.PrivacyField.class);

            // If the field doesn't have a privacy annotation, skip it entirely!
            if (privacyField == null) {
                continue;
            }

            // 3. Determine which Table and Column to use
            String targetTable = defaultTableName; // Start with the class default
            String targetColumn = field.getName(); // Start with the Java variable name

            if (!privacyField.table().isEmpty()) {
                targetTable = privacyField.table(); // Override if a specific table was provided
            }
            if (!privacyField.column().isEmpty()) {
                targetColumn = privacyField.column(); // Override if a specific column was provided
            }

            // 4. Fetch the specific rules for the target table
            TableRule effectiveRule = privacyService.getTableRule(role, targetTable);

            // 5. Apply the mask if the column is listed in the XML
            if (effectiveRule != null && !effectiveRule.getMaskColumns().isEmpty()) {
                final String finalColNameToMatch = targetColumn;

                if (effectiveRule.getMaskColumns().stream().anyMatch(c -> c.equalsIgnoreCase(finalColNameToMatch))) {
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
}