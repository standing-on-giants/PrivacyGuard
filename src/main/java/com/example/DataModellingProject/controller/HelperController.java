package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.HelperSearchRequest;
import com.example.DataModellingProject.dto.HelperSearchResponse;
import com.example.DataModellingProject.model.Helper;
import com.example.DataModellingProject.service.HelperService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/helpers")
public class HelperController {
    private final HelperService helperService;
    private final ModelMapper modelMapper;

    public HelperController(HelperService helperService, ModelMapper modelMapper) {
        this.helperService = helperService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/search")
    public List<HelperSearchResponse> searchHelpers(@RequestBody HelperSearchRequest request) {
        List<Helper>helpers =  helperService.searchHelpers(request);
        return helpers.stream()
                .map(helper -> modelMapper.map(helper, HelperSearchResponse.class))
                .collect(Collectors.toList());
    }
}