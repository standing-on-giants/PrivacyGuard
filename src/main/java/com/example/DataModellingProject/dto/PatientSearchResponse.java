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
    @PrivacyField(table = "Patient", column = "patient_Id")
    private Integer patientId;
    
    @PrivacyField(table = "Patient", column = "pt_Name") // Mapping to XML rule column
    private String fName;
    
    @PrivacyField(table = "Patient", column = "LName")
    private String lName;
    
    @PrivacyField(table = "Patient", column = "Gender")
    private String gender;
    
    @PrivacyField(table = "Patient", column = "birth_date") // Mapping to XML rule column
    private LocalDate dateOfBirth;
    
    @PrivacyField(table = "Patient", column = "contact_No")
    private String contactNo;
    
    @PrivacyField(table = "Patient", column = "pt_Address") // Mapping to XML rule column
    private String ptAddress;
}