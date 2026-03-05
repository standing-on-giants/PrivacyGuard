package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.StaffShiftSearchRequest;
import com.example.DataModellingProject.dto.StaffShiftSearchResponse;
import com.example.DataModellingProject.model.StaffShift;
import com.example.DataModellingProject.repository.StaffShiftRepository;
import com.example.DataModellingProject.specification.StaffShiftSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffShiftService {
    private final StaffShiftRepository staffShiftRepository;
    private final ModelMapper modelMapper;

    public StaffShiftService(StaffShiftRepository staffShiftRepository, ModelMapper modelMapper) {
        this.staffShiftRepository = staffShiftRepository;
        this.modelMapper = modelMapper;
    }

    public List<StaffShiftSearchResponse> searchStaffShifts(StaffShiftSearchRequest request) {
        Specification<StaffShift> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(StaffShiftSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(StaffShiftSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(StaffShiftSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(StaffShiftSpecification.hasShiftDateIn(request.getExactShiftDates()))
                .and(StaffShiftSpecification.hasShiftDateBetween(request.getShiftDateStart(), request.getShiftDateEnd()))
                .and(StaffShiftSpecification.hasShiftStartBetween(request.getShiftStartStart(), request.getShiftStartEnd()))
                .and(StaffShiftSpecification.hasShiftEndBetween(request.getShiftEndStart(), request.getShiftEndEnd()));

        List<StaffShift> shifts = staffShiftRepository.findAll(spec);
        return shifts.stream()
                .map(shift -> modelMapper.map(shift, StaffShiftSearchResponse.class))
                .collect(Collectors.toList());
    }
}