package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.DoctorSearchRequest;
import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.repository.DoctorRepository;
import com.example.DataModellingProject.specification.DoctorSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> searchDoctors(DoctorSearchRequest request) {
        Specification<Doctor> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(DoctorSpecification.hasFNameIn(request.getFNames()))
                .and(DoctorSpecification.hasLNameIn(request.getLNames()))
                .and(DoctorSpecification.hasGenderIn(request.getGenders()))
                .and(DoctorSpecification.hasSurgeonTypeIn(request.getSurgeonTypes()))
                .and(DoctorSpecification.hasDepartmentNameIn(request.getDepartmentNames()))
                .and(DoctorSpecification.hasRoomNoIn(request.getRoomNumbers()));

        return doctorRepository.findAll(spec);
    }
}