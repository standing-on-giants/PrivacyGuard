package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.StaffShiftSearchRequest;
import com.example.DataModellingProject.dto.StaffShiftSearchResponse;
import com.example.DataModellingProject.service.StaffShiftService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-shifts")
public class StaffShiftController {
    private final StaffShiftService staffShiftService;

    public StaffShiftController(StaffShiftService staffShiftService) {
        this.staffShiftService = staffShiftService;
    }

    @PostMapping("/search")
    public List<StaffShiftSearchResponse> searchStaffShifts(@RequestBody StaffShiftSearchRequest request) {
        return staffShiftService.searchStaffShifts(request);
    }
}