package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.RoomRecordSearchRequest;
import com.example.DataModellingProject.dto.RoomRecordSearchResponse;
import com.example.DataModellingProject.service.RoomRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-records")
public class RoomRecordController {
    private final RoomRecordService roomRecordService;

    public RoomRecordController(RoomRecordService roomRecordService) {
        this.roomRecordService = roomRecordService;
    }

    @PostMapping("/search")
    public List<RoomRecordSearchResponse> searchRoomRecords(@RequestBody RoomRecordSearchRequest request) {
        return roomRecordService.searchRoomRecords(request);
    }
}