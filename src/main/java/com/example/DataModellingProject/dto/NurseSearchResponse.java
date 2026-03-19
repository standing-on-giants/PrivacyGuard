package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Nurse")
public class NurseSearchResponse {
    @PrivacyField(table = "Nurse", column = "nurse_Id")
    private Integer nurseId;
    
    @PrivacyField(table = "Nurse", column = "FName")
    private String fName;
    
    @PrivacyField(table = "Nurse", column = "LName")
    private String lName;
    
    @PrivacyField(table = "Nurse", column = "Gender")
    private String gender;
    
    @PrivacyField(table = "Nurse", column = "contact_No")
    private String contactNo;
    
    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;
}