package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.MedicalRecordSearchRequest;
import com.example.DataModellingProject.dto.MedicalRecordSearchResponse;
import com.example.DataModellingProject.model.MedicalRecord;
import com.example.DataModellingProject.service.MedicalRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/medical-records")
public class RawMedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final ModelMapper modelMapper;

    public RawMedicalRecordController(MedicalRecordService medicalRecordService, ModelMapper modelMapper) {
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