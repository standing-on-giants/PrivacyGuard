package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.AppointmentSearchRequest;
import com.example.DataModellingProject.model.Appointment;
import com.example.DataModellingProject.repository.AppointmentRepository;
import com.example.DataModellingProject.specification.AppointmentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> searchAppointments(AppointmentSearchRequest request) {
        Specification<Appointment> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(AppointmentSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(AppointmentSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(AppointmentSpecification.hasModeOfAppointmentIn(request.getModesOfAppointment()))
                .and(AppointmentSpecification.hasAppointmentStatusIn(request.getAppointmentStatuses()))
                .and(AppointmentSpecification.hasAppointmentDateBetween(request.getDateStart(), request.getDateEnd()));

        return appointmentRepository.findAll(spec);
    }
}