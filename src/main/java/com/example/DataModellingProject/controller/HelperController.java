package com.example.DataModellingProject.controller;

import com.example.DataModellingProject.dto.HelperSearchRequest;
import com.example.DataModellingProject.dto.HelperSearchResponse;
import com.example.DataModellingProject.service.HelperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpers")
public class HelperController {
    private final HelperService helperService;

    public HelperController(HelperService helperService) {
        this.helperService = helperService;
    }

    @PostMapping("/search")
    public List<HelperSearchResponse> searchHelpers(@RequestBody HelperSearchRequest request) {
        return helperService.searchHelpers(request);
    }
}