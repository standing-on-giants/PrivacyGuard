package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.HelperSearchRequest;
import com.example.DataModellingProject.model.Helper;
import com.example.DataModellingProject.repository.HelperRepository;
import com.example.DataModellingProject.specification.HelperSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelperService {
    private final HelperRepository helperRepository;
    private final ModelMapper modelMapper;

    public HelperService(HelperRepository helperRepository, ModelMapper modelMapper) {
        this.helperRepository = helperRepository;
        this.modelMapper = modelMapper;
    }


    public List<Helper> searchHelpers(HelperSearchRequest request) {
        Specification<Helper> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(HelperSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(HelperSpecification.hasFNameIn(request.getFNames()))
                .and(HelperSpecification.hasLNameIn(request.getLNames()))
                .and(HelperSpecification.hasGenderIn(request.getGenders()))
                .and(HelperSpecification.hasDepartmentNameIn(request.getDepartmentNames()));

        return helperRepository.findAll(spec);
    }
}