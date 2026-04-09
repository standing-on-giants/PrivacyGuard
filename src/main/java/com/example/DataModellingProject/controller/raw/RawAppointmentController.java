// controller/raw/RawAppointmentController.java
package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.AppointmentSearchRequest;
import com.example.DataModellingProject.dto.AppointmentSearchResponse;
import com.example.DataModellingProject.model.Appointment;
import com.example.DataModellingProject.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/appointments")  // ← only change from original
public class RawAppointmentController {

    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    public RawAppointmentController(AppointmentService appointmentService, ModelMapper modelMapper) {
        this.appointmentService = appointmentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<AppointmentSearchResponse> searchAppointments(@RequestBody AppointmentSearchRequest request) {
        List<Appointment> appointments = appointmentService.searchAppointments(request);
        return appointments.stream()
                .map(a -> modelMapper.map(a, AppointmentSearchResponse.class))
                .collect(Collectors.toList());
    }
}