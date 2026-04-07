package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.NurseSearchRequest;
import com.example.DataModellingProject.model.Nurse;
import com.example.DataModellingProject.repository.NurseRepository;
import com.example.DataModellingProject.specification.NurseSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NurseService {
    private final NurseRepository nurseRepository;

    public NurseService(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    public List<Nurse> searchNurses(NurseSearchRequest request) {
        Specification<Nurse> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(NurseSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(NurseSpecification.hasFNameIn(request.getFNames()))
                .and(NurseSpecification.hasLNameIn(request.getLNames()))
                .and(NurseSpecification.hasGenderIn(request.getGenders()))
                .and(NurseSpecification.hasDepartmentNameIn(request.getDepartmentNames()));

        return nurseRepository.findAll(spec);
    }
}