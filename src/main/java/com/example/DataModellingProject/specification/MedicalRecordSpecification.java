package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.MedicalRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecordSpecification {

    public static Specification<MedicalRecord> hasDoctorIdIn(List<Integer> doctorIds) {
        return (root, query, criteriaBuilder) -> {
            if (doctorIds == null || doctorIds.isEmpty()) {
                return null;
            }
            return root.join("doctor").get("doctId").in(doctorIds);
        };
    }

    public static Specification<MedicalRecord> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }
            return root.join("patient").get("patientId").in(patientIds);
        };
    }

    public static Specification<MedicalRecord> hasVisitDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("visitDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("visitDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("visitDate"), endDate);
        };
    }

    public static Specification<MedicalRecord> hasNextVisitBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("nextVisit"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("nextVisit"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("nextVisit"), endDate);
        };
    }
}