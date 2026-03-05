package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.PatientSearchRequest;
import com.example.DataModellingProject.dto.PatientSearchResponse;
import com.example.DataModellingProject.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/search")
    public List<PatientSearchResponse> searchPatients(@RequestBody PatientSearchRequest request) {
        return patientService.searchPatients(request);
    }
}