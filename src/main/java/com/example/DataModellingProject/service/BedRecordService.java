package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.BedRecordSearchRequest;
import com.example.DataModellingProject.dto.BedRecordSearchResponse;
import com.example.DataModellingProject.model.BedRecord;
import com.example.DataModellingProject.repository.BedRecordRepository;
import com.example.DataModellingProject.specification.BedRecordSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedRecordService {
    private final BedRecordRepository bedRecordRepository;
    private final ModelMapper modelMapper;

    public BedRecordService(BedRecordRepository bedRecordRepository, ModelMapper modelMapper) {
        this.bedRecordRepository = bedRecordRepository;
        this.modelMapper = modelMapper;
    }

    public List<BedRecordSearchResponse> searchBedRecords(BedRecordSearchRequest request) {
        Specification<BedRecord> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(BedRecordSpecification.hasBedNoIn(request.getBedNos()))
                .and(BedRecordSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(BedRecordSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(BedRecordSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(BedRecordSpecification.hasAdmissionDateBetween(request.getAdmissionDateStart(), request.getAdmissionDateEnd()))
                .and(BedRecordSpecification.hasDischargeDateBetween(request.getDischargeDateStart(), request.getDischargeDateEnd()))
                .and(BedRecordSpecification.hasAmountBetween(request.getMinAmount(), request.getMaxAmount()));

        List<BedRecord> records = bedRecordRepository.findAll(spec);
        return records.stream()
                .map(record -> modelMapper.map(record, BedRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}