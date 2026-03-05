package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class PatientSpecification {

    public static Specification<Patient> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }
            return root.get("patientId").in(patientIds);
        };
    }

    public static Specification<Patient> hasFNameIn(List<String> fNames) {
        return (root, query, criteriaBuilder) -> {
            if (fNames == null || fNames.isEmpty()) {
                return null;
            }
            return root.get("fName").in(fNames);
        };
    }

    public static Specification<Patient> hasLNameIn(List<String> lNames) {
        return (root, query, criteriaBuilder) -> {
            if (lNames == null || lNames.isEmpty()) {
                return null;
            }
            return root.get("lName").in(lNames);
        };
    }

    public static Specification<Patient> hasGenderIn(List<String> genders) {
        return (root, query, criteriaBuilder) -> {
            if (genders == null || genders.isEmpty()) {
                return null;
            }
            return root.get("gender").in(genders);
        };
    }

    public static Specification<Patient> hasDateOfBirthBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("dateOfBirth"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"), endDate);
        };
    }
}