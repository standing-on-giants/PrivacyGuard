package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.MedicalRecordSearchRequest;
import com.example.DataModellingProject.dto.MedicalRecordSearchResponse;
import com.example.DataModellingProject.model.MedicalRecord;
import com.example.DataModellingProject.service.MedicalRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final ModelMapper modelMapper;

    public MedicalRecordController(MedicalRecordService medicalRecordService, ModelMapper modelMapper) {
        this.medicalRecordService = medicalRecordService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<MedicalRecordSearchResponse> searchMedicalRecords(@RequestBody MedicalRecordSearchRequest request) {
        List<MedicalRecord> records = medicalRecordService.searchMedicalRecords(request);
        return records.stream()
                .map(record -> modelMapper.map(record, MedicalRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}