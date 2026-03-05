package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.MedicalRecordSearchRequest;
import com.example.DataModellingProject.dto.MedicalRecordSearchResponse;
import com.example.DataModellingProject.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/search")
    public List<MedicalRecordSearchResponse> searchMedicalRecords(@RequestBody MedicalRecordSearchRequest request) {
        return medicalRecordService.searchMedicalRecords(request);
    }
}