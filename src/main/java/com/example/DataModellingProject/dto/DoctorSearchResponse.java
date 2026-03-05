package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorSearchResponse {
    private Integer doctId;
    private String fName;
    private String lName;
    private String gender;
    private String contactNo;
    private String surgeonType;
    private String deptName;
    private Integer roomNo;
}