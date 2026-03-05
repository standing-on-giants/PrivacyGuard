package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.AppointmentSearchRequest;
import com.example.DataModellingProject.dto.AppointmentSearchResponse;
import com.example.DataModellingProject.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/search")
    public List<AppointmentSearchResponse> searchAppointments(@RequestBody AppointmentSearchRequest request) {
        return appointmentService.searchAppointments(request);
    }
}