package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.MedicalRecordSearchRequest;
import com.example.DataModellingProject.dto.MedicalRecordSearchResponse;
import com.example.DataModellingProject.model.MedicalRecord;
import com.example.DataModellingProject.repository.MedicalRecordRepository;
import com.example.DataModellingProject.specification.MedicalRecordSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final ModelMapper modelMapper;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, ModelMapper modelMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.modelMapper = modelMapper;
    }

    public List<MedicalRecordSearchResponse> searchMedicalRecords(MedicalRecordSearchRequest request) {
        Specification<MedicalRecord> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(MedicalRecordSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(MedicalRecordSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(MedicalRecordSpecification.hasVisitDateBetween(request.getVisitDateStart(), request.getVisitDateEnd()))
                .and(MedicalRecordSpecification.hasNextVisitBetween(request.getNextVisitStart(), request.getNextVisitEnd()));

        List<MedicalRecord> records = medicalRecordRepository.findAll(spec);
        return records.stream()
                .map(record -> modelMapper.map(record, MedicalRecordSearchResponse.class))
                .collect(Collectors.toList());
    }
}