package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DoctorSearchRequest {
    private List<String> fNames;
    private List<String> lNames;
    private List<String> genders;
    private List<String> surgeonTypes;
    private List<String> departmentNames;
    private List<Integer> roomNumbers;
}