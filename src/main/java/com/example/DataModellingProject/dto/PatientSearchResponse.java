package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("Patient")
public class PatientSearchResponse {
    private Integer patientId;
    private String fName;
    private String lName;
    private String gender;
    private LocalDate dateOfBirth;
    private String contactNo;
    private String ptAddress;
}