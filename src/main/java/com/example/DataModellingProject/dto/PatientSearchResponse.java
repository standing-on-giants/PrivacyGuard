package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("Patient")
public class PatientSearchResponse {

    @PrivacyField(column = "patient_Id")
    private Integer patientId;

    @PrivacyField(column = "FName")
    private String fName;

    @PrivacyField(column = "LName")
    private String lName;

    @PrivacyField(column = "Gender")
    private String gender;

    @PrivacyField(column = "Date_Of_Birth")
    private LocalDate dateOfBirth;

    @PrivacyField(column = "contact_No")
    private String contactNo;

    @PrivacyField(column = "pt_Address")
    private String ptAddress;
}