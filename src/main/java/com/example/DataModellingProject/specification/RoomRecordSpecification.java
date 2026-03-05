package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.RoomRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class RoomRecordSpecification {

    public static Specification<RoomRecord> hasRoomNoIn(List<Integer> roomNos) {
        return (root, query, criteriaBuilder) -> {
            if (roomNos == null || roomNos.isEmpty()) {
                return null;
            }
            return root.join("room").get("roomNo").in(roomNos);
        };
    }

    public static Specification<RoomRecord> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }
            return root.join("patient").get("patientId").in(patientIds);
        };
    }

    public static Specification<RoomRecord> hasNurseIdIn(List<Integer> nurseIds) {
        return (root, query, criteriaBuilder) -> {
            if (nurseIds == null || nurseIds.isEmpty()) {
                return null;
            }
            return root.join("nurse").get("nurseId").in(nurseIds);
        };
    }

    public static Specification<RoomRecord> hasHelperIdIn(List<Integer> helperIds) {
        return (root, query, criteriaBuilder) -> {
            if (helperIds == null || helperIds.isEmpty()) {
                return null;
            }
            return root.join("helper").get("helperId").in(helperIds);
        };
    }

    public static Specification<RoomRecord> hasAdmissionDateBetween(LocalDate startDate, LocalDate endDate) {
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

    public static Specification<RoomRecord> hasDischargeDateBetween(LocalDate startDate, LocalDate endDate) {
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
}