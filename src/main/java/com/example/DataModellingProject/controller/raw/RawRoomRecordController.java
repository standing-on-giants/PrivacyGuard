package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.RoomRecordSearchRequest;
import com.example.DataModellingProject.dto.RoomRecordSearchResponse;
import com.example.DataModellingProject.model.RoomRecord;
import com.example.DataModellingProject.service.RoomRecordService;
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

    public RawRoomRecordController(RoomRecordService roomRecordService) {
        this.roomRecordService = roomRecordService;
    }

    @PostMapping("/search")
    public List<RoomRecordSearchResponse> searchRoomRecords(
            @RequestBody RoomRecordSearchRequest request) {
        return roomRecordService.searchRoomRecords(request)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RoomRecordSearchResponse toResponse(RoomRecord record) {
        RoomRecordSearchResponse dto = new RoomRecordSearchResponse();

        dto.setRecordId(record.getAdmissionId());
        dto.setAdmissionDate(record.getAdmissionDate());
        dto.setDischargeDate(record.getDischargeDate());

        if (record.getRoom() != null) {
            dto.setRoomNo(record.getRoom().getRoomNo());
        }
        if (record.getPatient() != null) {
            dto.setPatientFirstName(record.getPatient().getFName());
            dto.setPatientLastName(record.getPatient().getLName());
        }
        if (record.getNurse() != null) {
            dto.setNurseFirstName(record.getNurse().getFName());
            dto.setNurseLastName(record.getNurse().getLName());
        }

        return dto;
    }
}