package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.StaffShift;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StaffShiftSpecification {

    public static Specification<StaffShift> hasDoctorIdIn(List<Integer> doctorIds) {
        return (root, query, criteriaBuilder) -> {
            if (doctorIds == null || doctorIds.isEmpty()) {
                return null;
            }
            return root.join("doctor").get("doctId").in(doctorIds);
        };
    }

    public static Specification<StaffShift> hasNurseIdIn(List<Integer> nurseIds) {
        return (root, query, criteriaBuilder) -> {
            if (nurseIds == null || nurseIds.isEmpty()) {
                return null;
            }
            return root.join("nurse").get("nurseId").in(nurseIds);
        };
    }

    public static Specification<StaffShift> hasHelperIdIn(List<Integer> helperIds) {
        return (root, query, criteriaBuilder) -> {
            if (helperIds == null || helperIds.isEmpty()) {
                return null;
            }
            return root.join("helper").get("helperId").in(helperIds);
        };
    }

    public static Specification<StaffShift> hasShiftDateIn(List<LocalDate> shiftDates) {
        return (root, query, criteriaBuilder) -> {
            if (shiftDates == null || shiftDates.isEmpty()) {
                return null;
            }
            return root.get("shiftDate").in(shiftDates);
        };
    }

    public static Specification<StaffShift> hasShiftDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("shiftDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("shiftDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("shiftDate"), endDate);
        };
    }

    public static Specification<StaffShift> hasShiftStartBetween(LocalTime startTime, LocalTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (startTime == null && endTime == null) {
                return null;
            }
            if (startTime != null && endTime != null) {
                return criteriaBuilder.between(root.get("shiftStart"), startTime, endTime);
            }
            if (startTime != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("shiftStart"), startTime);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("shiftStart"), endTime);
        };
    }

    public static Specification<StaffShift> hasShiftEndBetween(LocalTime startTime, LocalTime endTime) {
        return (root, query, criteriaBuilder) -> {
            if (startTime == null && endTime == null) {
                return null;
            }
            if (startTime != null && endTime != null) {
                return criteriaBuilder.between(root.get("shiftEnd"), startTime, endTime);
            }
            if (startTime != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("shiftEnd"), startTime);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("shiftEnd"), endTime);
        };
    }
}