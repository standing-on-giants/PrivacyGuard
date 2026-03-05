package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.PatientSearchRequest;
import com.example.DataModellingProject.dto.PatientSearchResponse;
import com.example.DataModellingProject.model.Patient;
import com.example.DataModellingProject.repository.PatientRepository;
import com.example.DataModellingProject.specification.PatientSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public PatientService(PatientRepository patientRepository, ModelMapper modelMapper) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }

    public List<PatientSearchResponse> searchPatients(PatientSearchRequest request) {
        Specification<Patient> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(PatientSpecification.hasPatientIdIn(request.getPatientIds()))
                .and(PatientSpecification.hasFNameIn(request.getFNames()))
                .and(PatientSpecification.hasLNameIn(request.getLNames()))
                .and(PatientSpecification.hasGenderIn(request.getGenders()))
                .and(PatientSpecification.hasDateOfBirthBetween(request.getDobStart(), request.getDobEnd()));

        List<Patient> patients = patientRepository.findAll(spec);
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientSearchResponse.class))
                .collect(Collectors.toList());
    }
}