package com.example.DataModellingProject.privacy.aspect;

import com.example.DataModellingProject.privacy.model.*;
import com.example.DataModellingProject.privacy.service.PrivacyService;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import com.example.DataModellingProject.context.UserContext;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

@Aspect
@Component
public class RowLevelSecurityAspect {

    private final EntityManager entityManager;
    private final PrivacyService privacyService;
    private final UserContext userContext;

    public RowLevelSecurityAspect(EntityManager entityManager,
                                  PrivacyService privacyService,
                                  UserContext userContext) {
        this.entityManager = entityManager;
        this.privacyService = privacyService;
        this.userContext = userContext;
    }

    @Around("execution(* com.example.DataModellingProject.service.*.*(..))")
    public Object filterRows(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        // RAW ENDPOINT BYPASS
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getRequestURI().contains("/raw/")) {
            return result;
        }

        if (!userContext.isAuthenticated()) {
            return result;
        }

        String role = userContext.getRole();

        if (userContext.getDatabaseId() == null) {
            if (result instanceof Collection) return java.util.Collections.emptyList();
            return null;
        }

        if (result instanceof Collection) {
            Collection<?> collection = (Collection<?>) result;
            Iterator<?> iterator = collection.iterator();

            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item != null && item.getClass().isAnnotationPresent(Entity.class)) {
                    entityManager.detach(item);
                }

                // Pass the entity to the new AST evaluator
                if (!evaluateAST(item, role)) {
                    iterator.remove();
                }
            }
        } else if (result != null) {
            if (result.getClass().isAnnotationPresent(Entity.class)) {
                entityManager.detach(result);
            }

            if (!evaluateAST(result, role)) {
                return null;
            }
        }

        return result;
    }

    private boolean evaluateAST(Object entity, String role) {
        if (entity == null) return false;

        PrivacyTable tableAnnotation = entity.getClass().getAnnotation(PrivacyTable.class);
        String tableName = (tableAnnotation != null) ? tableAnnotation.value() : entity.getClass().getSimpleName();

        TableRule rule = privacyService.getTableRule(role, tableName);

        // If no rule or row filter exists, default allow
        if (rule == null || rule.getRowFilter() == null || rule.getRowFilter().isEmpty()) {
            return true;
        }

        RowFilter filter = rule.getRowFilter();

        // Start the recursive evaluation
        if (filter.getLogic() != null) {
            return evaluateLogic(filter.getLogic(), entity);
        } else if (filter.getCondition() != null) {
            return evaluateCondition(filter.getCondition(), entity);
        }

        return true;
    }

    private boolean evaluateLogic(Logic logic, Object entity) {
        boolean isAnd = "AND".equalsIgnoreCase(logic.getOperator());
        boolean result = isAnd; // AND starts true, OR starts false

        // Evaluate all direct conditions in this block
        for (Condition cond : logic.getConditions()) {
            boolean condResult = evaluateCondition(cond, entity);
            result = isAnd ? (result && condResult) : (result || condResult);
        }

        // Recursively evaluate nested logic blocks
        for (Logic nestedLogic : logic.getLogics()) {
            boolean logicResult = evaluateLogic(nestedLogic, entity);
            result = isAnd ? (result && logicResult) : (result || logicResult);
        }

        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean evaluateCondition(Condition condition, Object entity) {
        // Resolve exactly what the Left and Right sides of the equation are
        Object left = resolveOperand(condition.getLeftOperand(), entity);
        Object right = resolveOperand(condition.getRightOperand(), entity);

        String op = condition.getOperator().toLowerCase();

        // Handle Null checks first
        if (left == null || right == null) {
            if (op.equals("is") || op.equals("eq")) return left == right;
            if (op.equals("is_not") || op.equals("neq")) return left != right;
            return false;
        }

        // Handle Equality
        if (op.equals("eq")) return left.toString().equals(right.toString());
        if (op.equals("neq")) return !left.toString().equals(right.toString());

        // Handle Mathematical / Date Comparisons (gt, lt, ge, le)
        if (left instanceof Comparable && right instanceof Comparable) {
            try {
                int cmp = ((Comparable) left).compareTo(right);
                switch (op) {
                    case "gt": return cmp > 0;
                    case "ge": return cmp >= 0;
                    case "lt": return cmp < 0;
                    case "le": return cmp <= 0;
                }
            } catch (ClassCastException e) {
                // If types don't match (e.g. comparing String to LocalDate), fail safely
                return false;
            }
        }

        // Handle Lists (in, not_in)
        if (right instanceof ListValue) {
            ListValue list = (ListValue) right;
            boolean contains = list.getStringValues().contains(left.toString()) ||
                    (left instanceof Integer && list.getIntegerValues().contains(left));

            if (op.equals("in")) return contains;
            if (op.equals("not_in")) return !contains;
        }

        return false;
    }


    private Object resolveOperand(Operand operand, Object entity) {
        if (operand == null) return null;

        // If it's a ContextAttribute, grab it from the UserContext
        if (operand.getContextAttribute() != null) {
            if ("databaseId".equals(operand.getContextAttribute())) {
                return userContext.getDatabaseId();
            }
            return userContext.getAttribute(operand.getContextAttribute());
        }

        // If it's a Column, extract it from the Database Entity using Reflection
        if (operand.getColumn() != null) {
            return extractFieldValue(entity, operand.getColumn());
        }

        // Otherwise, it's a raw XML value (DateValue, StringValue, etc.)
        return operand.getActualValue();
    }

    private Object extractFieldValue(Object entity, String targetColumn) {
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            boolean isMatch = false;

            Column columnOpt = field.getAnnotation(Column.class);
            if (columnOpt != null && columnOpt.name().equalsIgnoreCase(targetColumn)) isMatch = true;

            JoinColumn joinColumnOpt = field.getAnnotation(JoinColumn.class);
            if (joinColumnOpt != null && joinColumnOpt.name().equalsIgnoreCase(targetColumn)) isMatch = true;

            if (!isMatch && field.getName().equalsIgnoreCase(targetColumn.replace("_", ""))) isMatch = true;

            if (isMatch) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value != null) {
                        try {
                            // Let JPA figure out if it's an entity/proxy and grab the ID
                            Object nestedId = entityManager.getEntityManagerFactory()
                                    .getPersistenceUnitUtil()
                                    .getIdentifier(value);

                            // If JPA successfully extracted an ID, return that instead of the whole object!
                            if (nestedId != null) {
                                return nestedId;
                            }
                        } catch (IllegalArgumentException e) {
                            // If it throws this, 'value' is just a normal String, Integer, LocalDate, etc.
                            // We just catch it silently and fall through to return the raw value.
                        }

                        return value;
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Could not extract field value: " + field.getName());
                }
            }
        }
        return null;
    }
}