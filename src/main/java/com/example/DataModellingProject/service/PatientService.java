package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.PatientSearchRequest;
import com.example.DataModellingProject.model.Patient;
import com.example.DataModellingProject.repository.PatientRepository;
import com.example.DataModellingProject.specification.PatientSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public List<Patient> searchPatients(PatientSearchRequest request) {
        Specification<Patient> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(PatientSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(PatientSpecification.hasFNameIn(request.getFNames()))
                .and(PatientSpecification.hasLNameIn(request.getLNames()))
                .and(PatientSpecification.hasGenderIn(request.getGenders()))
                .and(PatientSpecification.hasDateOfBirthBetween(request.getDobStart(), request.getDobEnd()));

        return patientRepository.findAll(spec);
    }
}