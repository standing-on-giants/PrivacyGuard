package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperSearchResponse {
    private Integer helperId;
    private String fName;
    private String lName;
    private String gender;
    private String contactNo;
    private String deptName;
}