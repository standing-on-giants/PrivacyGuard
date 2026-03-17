package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Doctor")
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