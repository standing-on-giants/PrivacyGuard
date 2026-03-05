package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.SurgeryRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SurgeryRecordSpecification {

    public static Specification<SurgeryRecord> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }
            return root.join("patient").get("patientId").in(patientIds);
        };
    }

    public static Specification<SurgeryRecord> hasSurgeonIdIn(List<Integer> surgeonIds) {
        return (root, query, criteriaBuilder) -> {
            if (surgeonIds == null || surgeonIds.isEmpty()) {
                return null;
            }
            // Joins to the 'surgeon' field (which maps to the Doctor entity's doctId)
            return root.join("surgeon").get("doctId").in(surgeonIds);
        };
    }

    public static Specification<SurgeryRecord> hasRoomNoIn(List<Integer> roomNos) {
        return (root, query, criteriaBuilder) -> {
            if (roomNos == null || roomNos.isEmpty()) {
                return null;
            }
            return root.join("room").get("roomNo").in(roomNos);
        };
    }

    public static Specification<SurgeryRecord> hasNurseIdIn(List<Integer> nurseIds) {
        return (root, query, criteriaBuilder) -> {
            if (nurseIds == null || nurseIds.isEmpty()) {
                return null;
            }
            return root.join("nurse").get("nurseId").in(nurseIds);
        };
    }

    public static Specification<SurgeryRecord> hasHelperIdIn(List<Integer> helperIds) {
        return (root, query, criteriaBuilder) -> {
            if (helperIds == null || helperIds.isEmpty()) {
                return null;
            }
            return root.join("helper").get("helperId").in(helperIds);
        };
    }

    public static Specification<SurgeryRecord> hasSurgeryTypeIn(List<String> surgeryTypes) {
        return (root, query, criteriaBuilder) -> {
            if (surgeryTypes == null || surgeryTypes.isEmpty()) {
                return null;
            }
            return root.get("surgeryType").in(surgeryTypes);
        };
    }

    public static Specification<SurgeryRecord> hasSurgeryTypeContainingIgnoreCase(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("surgeryType")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }

    public static Specification<SurgeryRecord> hasSurgeryDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("surgeryDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("surgeryDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("surgeryDate"), endDate);
        };
    }

    public static Specification<SurgeryRecord> hasStartTimeBetween(LocalTime startTime, LocalTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (startTime == null && endTime == null) {
                return null;
            }
            if (startTime != null && endTime != null) {
                return criteriaBuilder.between(root.get("startTime"), startTime, endTime);
            }
            if (startTime != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), endTime);
        };
    }

    public static Specification<SurgeryRecord> hasEndTimeBetween(LocalTime startTime, LocalTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (startTime == null && endTime == null) {
                return null;
            }
            if (startTime != null && endTime != null) {
                return criteriaBuilder.between(root.get("endTime"), startTime, endTime);
            }
            if (startTime != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), startTime);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endTime);
        };
    }
}