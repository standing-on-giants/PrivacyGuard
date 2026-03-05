package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.Nurse;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class NurseSpecification {

    public static Specification<Nurse> hasNurseIdIn(List<Integer> nurseIds) {
        return (root, query, criteriaBuilder) -> {
            if (nurseIds == null || nurseIds.isEmpty()) {
                return null;
            }
            return root.get("nurseId").in(nurseIds);
        };
    }

    public static Specification<Nurse> hasFNameIn(List<String> fNames) {
        return (root, query, criteriaBuilder) -> {
            if (fNames == null || fNames.isEmpty()) {
                return null;
            }
            return root.get("fName").in(fNames);
        };
    }

    public static Specification<Nurse> hasLNameIn(List<String> lNames) {
        return (root, query, criteriaBuilder) -> {
            if (lNames == null || lNames.isEmpty()) {
                return null;
            }
            return root.get("lName").in(lNames);
        };
    }

    public static Specification<Nurse> hasGenderIn(List<String> genders) {
        return (root, query, criteriaBuilder) -> {
            if (genders == null || genders.isEmpty()) {
                return null;
            }
            return root.get("gender").in(genders);
        };
    }

    public static Specification<Nurse> hasDepartmentNameIn(List<String> deptNames) {
        return (root, query, criteriaBuilder) -> {
            if (deptNames == null || deptNames.isEmpty()) {
                return null;
            }

            return root.join("department").get("deptName").in(deptNames);
        };
    }
}