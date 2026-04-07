package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.StaffShiftSearchRequest;
import com.example.DataModellingProject.model.StaffShift;
import com.example.DataModellingProject.repository.StaffShiftRepository;
import com.example.DataModellingProject.specification.StaffShiftSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffShiftService {
    private final StaffShiftRepository staffShiftRepository;

    public StaffShiftService(StaffShiftRepository staffShiftRepository) {
        this.staffShiftRepository = staffShiftRepository;
    }

    public List<StaffShift> searchStaffShifts(StaffShiftSearchRequest request) {
        Specification<StaffShift> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and(StaffShiftSpecification.hasDoctorIdIn(request.getDoctorIds()))
                .and(StaffShiftSpecification.hasNurseIdIn(request.getNurseIds()))
                .and(StaffShiftSpecification.hasHelperIdIn(request.getHelperIds()))
                .and(StaffShiftSpecification.hasShiftDateIn(request.getExactShiftDates()))
                .and(StaffShiftSpecification.hasShiftDateBetween(request.getShiftDateStart(), request.getShiftDateEnd()))
                .and(StaffShiftSpecification.hasShiftStartBetween(request.getShiftStartStart(), request.getShiftStartEnd()))
                .and(StaffShiftSpecification.hasShiftEndBetween(request.getShiftEndStart(), request.getShiftEndEnd()));

        return staffShiftRepository.findAll(spec);
    }
}