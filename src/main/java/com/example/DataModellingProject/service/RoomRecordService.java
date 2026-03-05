package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.RoomRecordSearchRequest;
import com.example.DataModellingProject.dto.RoomRecordSearchResponse;
import com.example.DataModellingProject.model.RoomRecord;
import com.example.DataModellingProject.repository.RoomRecordRepository;
import com.example.DataModellingProject.specification.RoomRecordSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomRecordService {
    private final RoomRecordRepository roomRecordRepository;
    private final ModelMapper modelMapper;

    public RoomRecordService(RoomRecordRepository roomRecordRepository, ModelMapper modelMapper) {
        this.roomRecordRepository = roomRecordRepository;
        this.modelMapper = modelMapper;
    }

    public List<RoomRecordSearchResponse> searchRoomRecords(RoomRecordSearchRequest request) {
        Specification<RoomRecord> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(RoomRecordSpecification.hasRoomNoIn(request.getRoomNos()))
                .and(RoomRecordSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(RoomRecordSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(RoomRecordSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(RoomRecordSpecification.hasAdmissionDateBetween(request.getAdmissionDateStart(), request.getAdmissionDateEnd()))
                .and(RoomRecordSpecification.hasDischargeDateBetween(request.getDischargeDateStart(), request.getDischargeDateEnd()));

        List<RoomRecord> records = roomRecordRepository.findAll(spec);
        return records.stream()
                .map(record -> modelMapper.map(record, RoomRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}