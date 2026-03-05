package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.SurgeryRecordSearchRequest;
import com.example.DataModellingProject.dto.SurgeryRecordSearchResponse;
import com.example.DataModellingProject.model.SurgeryRecord;
import com.example.DataModellingProject.repository.SurgeryRecordRepository;
import com.example.DataModellingProject.specification.SurgeryRecordSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurgeryRecordService {
    private final SurgeryRecordRepository surgeryRecordRepository;
    private final ModelMapper modelMapper;

    public SurgeryRecordService(SurgeryRecordRepository surgeryRecordRepository, ModelMapper modelMapper) {
        this.surgeryRecordRepository = surgeryRecordRepository;
        this.modelMapper = modelMapper;
    }

    public List<SurgeryRecordSearchResponse> searchSurgeries(SurgeryRecordSearchRequest request) {
        Specification<SurgeryRecord> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(SurgeryRecordSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(SurgeryRecordSpecification.hasSurgeonIdIn(request.getSurgeonIds()))
                .and(SurgeryRecordSpecification.hasRoomNoIn(request.getRoomNos()))
                .and(SurgeryRecordSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(SurgeryRecordSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(SurgeryRecordSpecification.hasSurgeryTypeIn(request.getSurgeryTypes()))
                .and(SurgeryRecordSpecification.hasSurgeryTypeContainingIgnoreCase(request.getSurgeryTypeKeyword()))
                .and(SurgeryRecordSpecification.hasSurgeryDateBetween(request.getSurgeryDateStart(), request.getSurgeryDateEnd()))
                .and(SurgeryRecordSpecification.hasStartTimeBetween(request.getStartTimeStart(), request.getStartTimeEnd()))
                .and(SurgeryRecordSpecification.hasEndTimeBetween(request.getEndTimeStart(), request.getEndTimeEnd()));

        List<SurgeryRecord> surgeries = surgeryRecordRepository.findAll(spec);
        return surgeries.stream()
                .map(surgery -> modelMapper.map(surgery, SurgeryRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}