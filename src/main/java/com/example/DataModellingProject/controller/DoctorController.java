package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.DoctorSearchRequest;
import com.example.DataModellingProject.dto.DoctorSearchResponse;
import com.example.DataModellingProject.service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/search")
    public List<DoctorSearchResponse> searchDoctors(@RequestBody DoctorSearchRequest request) {
        return doctorService.searchDoctors(request);
    }
}