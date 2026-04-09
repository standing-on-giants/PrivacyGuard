package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.RoomRecordSearchRequest;
import com.example.DataModellingProject.dto.RoomRecordSearchResponse;
import com.example.DataModellingProject.model.RoomRecord;
import com.example.DataModellingProject.service.RoomRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/room-records")
public class RawRoomRecordController {
    private final RoomRecordService roomRecordService;
    private final ModelMapper modelMapper;

    public RawRoomRecordController(RoomRecordService roomRecordService, ModelMapper modelMapper) {
        this.roomRecordService = roomRecordService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<RoomRecordSearchResponse> searchRoomRecords(@RequestBody RoomRecordSearchRequest request) {
        List<RoomRecord> records = roomRecordService.searchRoomRecords(request);
        return records.stream()
                .map(record -> modelMapper.map(record, RoomRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}