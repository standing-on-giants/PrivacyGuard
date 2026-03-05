package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.NurseSearchRequest;
import com.example.DataModellingProject.dto.NurseSearchResponse;
import com.example.DataModellingProject.model.Nurse;
import com.example.DataModellingProject.repository.NurseRepository;
import com.example.DataModellingProject.specification.NurseSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NurseService {
    private final NurseRepository nurseRepository;
    private final ModelMapper modelMapper;

    public NurseService(NurseRepository nurseRepository, ModelMapper modelMapper) {
        this.nurseRepository = nurseRepository;
        this.modelMapper = modelMapper;
    }

    public List<NurseSearchResponse> searchNurses(NurseSearchRequest request) {
        Specification<Nurse> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(NurseSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(NurseSpecification.hasFNameIn(request.getFNames()))
                .and(NurseSpecification.hasLNameIn(request.getLNames()))
                .and(NurseSpecification.hasGenderIn(request.getGenders()))
                .and(NurseSpecification.hasDepartmentNameIn(request.getDepartmentNames()));

        List<Nurse> nurses = nurseRepository.findAll(spec);
        return nurses.stream()
                .map(nurse -> modelMapper.map(nurse, NurseSearchResponse.class))
                .collect(Collectors.toList());
    }
}