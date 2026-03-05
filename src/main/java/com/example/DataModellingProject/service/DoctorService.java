package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.DoctorSearchRequest;
import com.example.DataModellingProject.dto.DoctorSearchResponse;
import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.repository.DoctorRepository;
import com.example.DataModellingProject.specification.DoctorSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public DoctorService(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    public List<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request) {
        Specification<Doctor> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(DoctorSpecification.hasFNameIn(request.getFNames()))
                .and(DoctorSpecification.hasLNameIn(request.getLNames()))
                .and(DoctorSpecification.hasGenderIn(request.getGenders()))
                .and(DoctorSpecification.hasSurgeonTypeIn(request.getSurgeonTypes()))
                .and(DoctorSpecification.hasDepartmentNameIn(request.getDepartmentNames()))
                .and(DoctorSpecification.hasRoomNoIn(request.getRoomNumbers()));

        List<Doctor> doctors = doctorRepository.findAll(spec);
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorSearchResponse.class))
                .collect(Collectors.toList());
    }
}