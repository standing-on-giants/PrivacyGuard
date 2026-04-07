package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.NurseSearchRequest;
import com.example.DataModellingProject.dto.NurseSearchResponse;
import com.example.DataModellingProject.model.Nurse;
import com.example.DataModellingProject.service.NurseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {
    private final NurseService nurseService;
    private final ModelMapper modelMapper;

    public NurseController(NurseService nurseService, ModelMapper modelMapper) {
        this.nurseService = nurseService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<NurseSearchResponse> searchNurses(@RequestBody NurseSearchRequest request) {
        List<Nurse> nurses = nurseService.searchNurses(request);
        return nurses.stream()
                .map(nurse -> modelMapper.map(nurse, NurseSearchResponse.class))
                .collect(Collectors.toList());
    }
}