package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Nurse")
public class NurseSearchResponse {
    private Integer nurseId;
    private String fName;
    private String lName;
    private String gender;
    private String contactNo;
    private String deptName;
}