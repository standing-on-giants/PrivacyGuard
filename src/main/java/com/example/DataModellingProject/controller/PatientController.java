package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.PatientSearchRequest;
import com.example.DataModellingProject.dto.PatientSearchResponse;
import com.example.DataModellingProject.model.Patient;
import com.example.DataModellingProject.service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    public PatientController(PatientService patientService, ModelMapper modelMapper) {
        this.patientService = patientService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<PatientSearchResponse> searchPatients(@RequestBody PatientSearchRequest request) {
       List<Patient> patients = patientService.searchPatients(request);
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientSearchResponse.class))
                .collect(Collectors.toList());
    }
}