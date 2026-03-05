package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseSearchResponse {
    private Integer nurseId;
    private String fName;
    private String lName;
    private String gender;
    private String contactNo;
    private String deptName;
}