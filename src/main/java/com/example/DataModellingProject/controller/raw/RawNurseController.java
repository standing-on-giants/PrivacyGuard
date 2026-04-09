package com.example.DataModellingProject.controller.raw;

import com.example.DataModellingProject.dto.NurseSearchRequest;
import com.example.DataModellingProject.dto.NurseSearchResponse;
import com.example.DataModellingProject.model.Nurse;
import com.example.DataModellingProject.service.NurseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/raw/nurses")
public class RawNurseController {
    private final NurseService nurseService;
    private final ModelMapper modelMapper;

    public RawNurseController(NurseService nurseService, ModelMapper modelMapper) {
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