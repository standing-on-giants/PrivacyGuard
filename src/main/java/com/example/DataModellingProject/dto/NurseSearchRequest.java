package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NurseSearchRequest {
    private List<Integer> nurseIds;
    private List<String> fNames;
    private List<String> lNames;
    private List<String> genders;
    private List<String> departmentNames;
}