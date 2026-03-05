package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class AppointmentSpecification {

    public static Specification<Appointment> hasPatientIdIn(List<Integer> patientIds) {
        return (root, query, criteriaBuilder) -> {
            if (patientIds == null || patientIds.isEmpty()) {
                return null;
            }

            return root.join("patient").get("patientId").in(patientIds);
        };
    }

    public static Specification<Appointment> hasDoctorIdIn(List<Integer> doctorIds) {
        return (root, query, criteriaBuilder) -> {
            if (doctorIds == null || doctorIds.isEmpty()) {
                return null;
            }

            return root.join("doctor").get("doctId").in(doctorIds);
        };
    }

    public static Specification<Appointment> hasModeOfAppointmentIn(List<String> modes) {
        return (root, query, criteriaBuilder) -> {
            if (modes == null || modes.isEmpty()) {
                return null;
            }

            return root.get("modeOfAppointment").in(modes);
        };
    }

    public static Specification<Appointment> hasAppointmentStatusIn(List<String> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return null;
            }

            return root.get("appointmentStatus").in(statuses);
        };
    }

    public static Specification<Appointment> hasAppointmentDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("appointmentDate"), startDate, endDate);
            }

            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("appointmentDate"), startDate);
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("appointmentDate"), endDate);
        };
    }
}