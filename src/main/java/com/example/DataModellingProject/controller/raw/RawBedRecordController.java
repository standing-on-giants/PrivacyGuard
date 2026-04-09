package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.BedRecordSearchRequest;
import com.example.DataModellingProject.dto.BedRecordSearchResponse;
import com.example.DataModellingProject.model.BedRecord;
import com.example.DataModellingProject.service.BedRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/bed-records")
public class RawBedRecordController {
    private final BedRecordService bedRecordService;
    private final ModelMapper modelMapper;

    public RawBedRecordController(BedRecordService bedRecordService, ModelMapper modelMapper) {
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