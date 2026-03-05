package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.BedRecordSearchRequest;
import com.example.DataModellingProject.dto.BedRecordSearchResponse;
import com.example.DataModellingProject.service.BedRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bed-records")
public class BedRecordController {
    private final BedRecordService bedRecordService;

    public BedRecordController(BedRecordService bedRecordService) {
        this.bedRecordService = bedRecordService;
    }

    @PostMapping("/search")
    public List<BedRecordSearchResponse> searchBedRecords(@RequestBody BedRecordSearchRequest request) {
        return bedRecordService.searchBedRecords(request);
    }
}