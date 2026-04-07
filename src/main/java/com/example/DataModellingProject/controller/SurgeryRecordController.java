package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.SurgeryRecordSearchRequest;
import com.example.DataModellingProject.dto.SurgeryRecordSearchResponse;
import com.example.DataModellingProject.model.SurgeryRecord;
import com.example.DataModellingProject.service.SurgeryRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/surgery-records")
public class SurgeryRecordController {
    private final SurgeryRecordService surgeryRecordService;
    private final ModelMapper modelMapper;

    public SurgeryRecordController(SurgeryRecordService surgeryRecordService, ModelMapper modelMapper) {
        this.surgeryRecordService = surgeryRecordService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<SurgeryRecordSearchResponse> searchSurgeries(@RequestBody SurgeryRecordSearchRequest request) {
        List<SurgeryRecord> surgeries = surgeryRecordService.searchSurgeries(request);
        return surgeries.stream()
                .map(surgery -> modelMapper.map(surgery, SurgeryRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}