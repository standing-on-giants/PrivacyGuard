package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.SurgeryRecordSearchRequest;
import com.example.DataModellingProject.dto.SurgeryRecordSearchResponse;
import com.example.DataModellingProject.service.SurgeryRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surgery-records")
public class SurgeryRecordController {
    private final SurgeryRecordService surgeryRecordService;

    public SurgeryRecordController(SurgeryRecordService surgeryRecordService) {
        this.surgeryRecordService = surgeryRecordService;
    }

    @PostMapping("/search")
    public List<SurgeryRecordSearchResponse> searchSurgeries(@RequestBody SurgeryRecordSearchRequest request) {
        return surgeryRecordService.searchSurgeries(request);
    }
}