package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.StaffShiftSearchRequest;
import com.example.DataModellingProject.dto.StaffShiftSearchResponse;
import com.example.DataModellingProject.model.StaffShift;
import com.example.DataModellingProject.service.StaffShiftService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/staff-shifts")
public class RawStaffShiftController {
    private final StaffShiftService staffShiftService;
    private final ModelMapper modelMapper;

    public RawStaffShiftController(StaffShiftService staffShiftService, ModelMapper modelMapper) {
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