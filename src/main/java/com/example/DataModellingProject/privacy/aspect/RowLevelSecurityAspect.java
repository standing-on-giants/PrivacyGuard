package com.example.DataModellingProject.privacy.aspect;

import com.example.DataModellingProject.privacy.model.TableRule;
import com.example.DataModellingProject.privacy.service.PrivacyService;
import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import com.example.DataModellingProject.repository.DoctorAccountRepository;
import com.example.DataModellingProject.repository.NurseAccountRepository;
import com.example.DataModellingProject.repository.PatientAccountRepository;
import com.example.DataModellingProject.repository.HelperAccountRepository;
import com.example.DataModellingProject.repository.AdminAccountRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

@Aspect
@Component
public class RowLevelSecurityAspect {

    private final EntityManager entityManager;
    private final PrivacyService privacyService;

    // 1. Inject all Account Repositories
    private final DoctorAccountRepository doctorAccountRepository;
    private final NurseAccountRepository nurseAccountRepository;
    private final PatientAccountRepository patientAccountRepository;
    private final HelperAccountRepository helperAccountRepository;
    private final AdminAccountRepository adminAccountRepository;

    public RowLevelSecurityAspect(EntityManager entityManager,
                                  PrivacyService privacyService,
                                  DoctorAccountRepository doctorAccountRepository,
                                  NurseAccountRepository nurseAccountRepository,
                                  PatientAccountRepository patientAccountRepository,
                                  HelperAccountRepository helperAccountRepository,
                                  AdminAccountRepository adminAccountRepository) {
        this.entityManager = entityManager;
        this.privacyService = privacyService;
        this.doctorAccountRepository = doctorAccountRepository;
        this.nurseAccountRepository = nurseAccountRepository;
        this.patientAccountRepository = patientAccountRepository;
        this.helperAccountRepository = helperAccountRepository;
        this.adminAccountRepository = adminAccountRepository;
    }

    @Around("execution(* com.example.DataModellingProject.service.*.*(..))")
    public Object filterRows(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return result;
        }

        String userEmail = auth.getName();
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("")
                .replace("ROLE_", "")
                .toLowerCase();

        // 2. Convert Email to Database ID based on their specific Role
        String loggedInUserId = null;

        switch (role) {
            case "doctor":
                var doctorOpt = doctorAccountRepository.findByEmail(userEmail);
                if (doctorOpt.isPresent()) loggedInUserId = String.valueOf(doctorOpt.get().getDoctorId());
                break;
            case "nurse":
                var nurseOpt = nurseAccountRepository.findByEmail(userEmail);
                if (nurseOpt.isPresent()) loggedInUserId = String.valueOf(nurseOpt.get().getNurseId());
                break;
            case "patient":
                var patientOpt = patientAccountRepository.findByEmail(userEmail);
                if (patientOpt.isPresent()) loggedInUserId = String.valueOf(patientOpt.get().getPatientId());
                break;
            case "helper":
                var helperOpt = helperAccountRepository.findByEmail(userEmail);
                if (helperOpt.isPresent()) loggedInUserId = String.valueOf(helperOpt.get().getHelperId());
                break;
            case "admin":
                var adminOpt = adminAccountRepository.findByEmail(userEmail);
                if (adminOpt.isPresent()) loggedInUserId = String.valueOf(adminOpt.get().getAdminId());
                break;
            default:
                System.out.println("RLS Warning: Unknown role detected -> " + role);
        }

        // 3. Block access if the user doesn't actually exist in the database
        if (loggedInUserId == null) {
            System.err.println("RLS Blocked: Could not find DB ID for email: " + userEmail + " with role: " + role);
            if (result instanceof Collection) return java.util.Collections.emptyList();
            return null;
        }

        // 4. Filter Lists
        if (result instanceof Collection) {
            Collection<?> collection = (Collection<?>) result;
            Iterator<?> iterator = collection.iterator();

            while (iterator.hasNext()) {
                Object item = iterator.next();

                // ONLY detach if it is actually a Hibernate database Entity
                if (item != null && item.getClass().isAnnotationPresent(Entity.class)) {
                    entityManager.detach(item);
                }

                // If they don't own it, delete it from the list
                if (!evaluateOwnership(item, role, loggedInUserId)) {
                    iterator.remove();
                }
            }
        }
        // 5. Filter Single Objects
        else if (result != null) {
            if (result.getClass().isAnnotationPresent(Entity.class)) {
                entityManager.detach(result);
            }

            if (!evaluateOwnership(result, role, loggedInUserId)) {
                return null;
            }
        }

        return result;
    }

    private boolean evaluateOwnership(Object entity, String role, String loggedInUserId) {
        if (entity == null) return false;

        PrivacyTable tableAnnotation = entity.getClass().getAnnotation(PrivacyTable.class);
        String tableName = (tableAnnotation != null) ? tableAnnotation.value() : entity.getClass().getSimpleName();

        TableRule rule = privacyService.getTableRule(role, tableName);

        if (rule == null || rule.getRowFilter() == null) {
            return true;
        }

        String ownershipColumn = privacyService.getOwnershipAttribute(role);

        System.out.println("RLS DEBUG -> User Token ID: [" + loggedInUserId + "] | Searching Table: [" + tableName + "] for XML Column: [" + ownershipColumn + "]");

        if (ownershipColumn == null) return false;

        boolean isOwner = checkFieldValueMatches(entity, ownershipColumn, loggedInUserId);

        System.out.println("RLS DEBUG -> Did row match? " + isOwner);

        return isOwner;
    }

    private boolean checkFieldValueMatches(Object entity, String targetXmlColumn, String loggedInUserId) {
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            boolean isMatch = false;

            // 1. Check native JPA @Column annotation
            Column columnOpt = field.getAnnotation(Column.class);
            if (columnOpt != null && columnOpt.name().equalsIgnoreCase(targetXmlColumn)) {
                isMatch = true;
            }

            // 2. Check native JPA @JoinColumn annotation (for foreign keys)
            JoinColumn joinColumnOpt = field.getAnnotation(JoinColumn.class);
            if (joinColumnOpt != null && joinColumnOpt.name().equalsIgnoreCase(targetXmlColumn)) {
                isMatch = true;
            }

            // 3. Fallback: Fuzzy match the variable name
            if (!isMatch && field.getName().equalsIgnoreCase(targetXmlColumn.replace("_", ""))) {
                isMatch = true;
            }

            // If we found the target column, extract its value!
            if (isMatch) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value != null) {

                        Object finalIdToCompare = null;

                        // SCENARIO A: The value is a nested Entity Object (like Doctor or Patient)
                        // Use JPA's native utility to safely extract the ID, even if it is a Lazy Proxy!
                        try {
                            finalIdToCompare = entityManager.getEntityManagerFactory()
                                    .getPersistenceUnitUtil()
                                    .getIdentifier(value);
                        }
                        // SCENARIO B: It's just a raw String or Integer ID
                        // If it's not an entity, getIdentifier throws an exception, so we fall back to the raw value.
                        catch (IllegalArgumentException e) {
                            finalIdToCompare = value;
                        }

                        // Finally, compare whatever ID we extracted to the logged in user!
                        if (finalIdToCompare != null && finalIdToCompare.toString().equals(loggedInUserId)) {
                            return true;
                        }
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("RowLevelSecurityAspect: Could not read ownership field: " + field.getName());
                }
            }
        }

        return false;
    }
}