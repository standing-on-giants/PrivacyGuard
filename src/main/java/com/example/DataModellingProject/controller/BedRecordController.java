package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.BedRecordSearchRequest;
import com.example.DataModellingProject.dto.BedRecordSearchResponse;
import com.example.DataModellingProject.model.BedRecord;
import com.example.DataModellingProject.service.BedRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bed-records")
public class BedRecordController {
    private final BedRecordService bedRecordService;
    private final ModelMapper modelMapper;

    public BedRecordController(BedRecordService bedRecordService, ModelMapper modelMapper) {
        this.bedRecordService = bedRecordService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<BedRecordSearchResponse> searchBedRecords(@RequestBody BedRecordSearchRequest request) {
        List<BedRecord> records = bedRecordService.searchBedRecords(request);
        return records.stream()
                .map(record -> modelMapper.map(record, BedRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}