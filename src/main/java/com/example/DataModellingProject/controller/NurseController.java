package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.NurseSearchRequest;
import com.example.DataModellingProject.dto.NurseSearchResponse;
import com.example.DataModellingProject.service.NurseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {
    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @PostMapping("/search")
    public List<NurseSearchResponse> searchNurses(@RequestBody NurseSearchRequest request) {
        return nurseService.searchNurses(request);
    }
}