package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.BedRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class BedRecordSpecification {

    public static Specification<BedRecord> hasBedNoIn(List<Integer> bedNos) {
        return (root, query, criteriaBuilder) -> {
            if (bedNos == null || bedNos.isEmpty()) {
                return null;
            }
            return root.join("bed").get("bedNo").in(bedNos);
        };
    }

    public static Specification<BedRecord> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }
            return root.join("patient").get("patientId").in(patientIds);
        };
    }

    public static Specification<BedRecord> hasNurseIdIn(List<Integer> nurseIds) {
        return (root, query, criteriaBuilder) -> {
            if (nurseIds == null || nurseIds.isEmpty()) {
                return null;
            }
            return root.join("nurse").get("nurseId").in(nurseIds);
        };
    }

    public static Specification<BedRecord> hasHelperIdIn(List<Integer> helperIds) {
        return (root, query, criteriaBuilder) -> {
            if (helperIds == null || helperIds.isEmpty()) {
                return null;
            }
            return root.join("helper").get("helperId").in(helperIds);
        };
    }

    public static Specification<BedRecord> hasAdmissionDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("admissionDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("admissionDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("admissionDate"), endDate);
        };
    }

    public static Specification<BedRecord> hasDischargeDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("dischargeDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dischargeDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dischargeDate"), endDate);
        };
    }

    public static Specification<BedRecord> hasAmountBetween(Integer minAmount, Integer maxAmount) {
        return (root, query, criteriaBuilder) -> {
            if (minAmount == null && maxAmount == null) {
                return null;
            }
            if (minAmount != null && maxAmount != null) {
                return criteriaBuilder.between(root.get("amount"), minAmount, maxAmount);
            }
            if (minAmount != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount);
        };
    }
}