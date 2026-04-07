package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.DoctorSearchRequest;
import com.example.DataModellingProject.dto.DoctorSearchResponse;
import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    public DoctorController(DoctorService doctorService, ModelMapper modelMapper) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<DoctorSearchResponse> searchDoctors(@RequestBody DoctorSearchRequest request) {
        List<Doctor> doctors = doctorService.searchDoctors(request);
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorSearchResponse.class))
                .collect(Collectors.toList());
    }
}