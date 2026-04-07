package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.MedicalRecordSearchRequest;
import com.example.DataModellingProject.model.MedicalRecord;
import com.example.DataModellingProject.repository.MedicalRecordRepository;
import com.example.DataModellingProject.specification.MedicalRecordSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> searchMedicalRecords(MedicalRecordSearchRequest request) {
        Specification<MedicalRecord> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(MedicalRecordSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(MedicalRecordSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(MedicalRecordSpecification.hasVisitDateBetween(request.getVisitDateStart(), request.getVisitDateEnd()))
                .and(MedicalRecordSpecification.hasNextVisitBetween(request.getNextVisitStart(), request.getNextVisitEnd()));

        return medicalRecordRepository.findAll(spec);
    }
}