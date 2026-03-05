package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.AppointmentSearchRequest;
import com.example.DataModellingProject.dto.AppointmentSearchResponse;
import com.example.DataModellingProject.model.Appointment;
import com.example.DataModellingProject.repository.AppointmentRepository;
import com.example.DataModellingProject.specification.AppointmentSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    public List<AppointmentSearchResponse> searchAppointments(AppointmentSearchRequest request) {
        Specification<Appointment> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(AppointmentSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(AppointmentSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(AppointmentSpecification.hasModeOfAppointmentIn(request.getModesOfAppointment()))
                .and(AppointmentSpecification.hasAppointmentStatusIn(request.getAppointmentStatuses()))
                .and(AppointmentSpecification.hasAppointmentDateBetween(request.getDateStart(), request.getDateEnd()));

        List<Appointment> appointments = appointmentRepository.findAll(spec);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentSearchResponse.class))
                .collect(Collectors.toList());
    }
}