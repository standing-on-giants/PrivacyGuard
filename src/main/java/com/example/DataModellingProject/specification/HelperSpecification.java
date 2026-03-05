package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.Helper;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class HelperSpecification {

    public static Specification<Helper> hasHelperIdIn(List<Integer> helperIds) {
        return (root, query, criteriaBuilder) -> {
            if (helperIds == null || helperIds.isEmpty()) {
                return null;
            }
            return root.get("helperId").in(helperIds);
        };
    }

    public static Specification<Helper> hasFNameIn(List<String> fNames) {
        return (root, query, criteriaBuilder) -> {
            if (fNames == null || fNames.isEmpty()) {
                return null;
            }
            return root.get("fName").in(fNames);
        };
    }

    public static Specification<Helper> hasLNameIn(List<String> lNames) {
        return (root, query, criteriaBuilder) -> {
            if (lNames == null || lNames.isEmpty()) {
                return null;
            }
            return root.get("lName").in(lNames);
        };
    }

    public static Specification<Helper> hasGenderIn(List<String> genders) {
        return (root, query, criteriaBuilder) -> {
            if (genders == null || genders.isEmpty()) {
                return null;
            }
            return root.get("gender").in(genders);
        };
    }

    public static Specification<Helper> hasDepartmentNameIn(List<String> deptNames) {
        return (root, query, criteriaBuilder) -> {
            if (deptNames == null || deptNames.isEmpty()) {
                return null;
            }

            return root.join("department").get("deptName").in(deptNames);
        };
    }
}