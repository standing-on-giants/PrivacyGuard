package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.StaffShiftSearchRequest;
import com.example.DataModellingProject.dto.StaffShiftSearchResponse;
import com.example.DataModellingProject.model.StaffShift;
import com.example.DataModellingProject.service.StaffShiftService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff-shifts")
public class StaffShiftController {
    private final StaffShiftService staffShiftService;
    private final ModelMapper modelMapper;

    public StaffShiftController(StaffShiftService staffShiftService, ModelMapper modelMapper) {
        this.staffShiftService = staffShiftService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<StaffShiftSearchResponse> searchStaffShifts(@RequestBody StaffShiftSearchRequest request) {
        List<StaffShift> shifts = staffShiftService.searchStaffShifts(request);
        return shifts.stream()
                .map(shift -> modelMapper.map(shift, StaffShiftSearchResponse.class))
                .collect(Collectors.toList());
    }
}