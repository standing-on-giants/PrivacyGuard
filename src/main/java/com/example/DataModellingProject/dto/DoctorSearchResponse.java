package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Doctor")
public class DoctorSearchResponse {
    @PrivacyField(table = "Doctor", column = "doct_Id")
    private Integer doctId;
    
    @PrivacyField(table = "Doctor", column = "FName")
    private String fName;
    
    @PrivacyField(table = "Doctor", column = "LName")
    private String lName;
    
    @PrivacyField(table = "Doctor", column = "Gender")
    private String gender;
    
    @PrivacyField(table = "Doctor", column = "contact_No")
    private String contactNo;
    
    @PrivacyField(table = "Doctor", column = "surgeon_Type")
    private String surgeonType;
    
    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;
    
    @PrivacyField(table = "Room", column = "room_No")
    private Integer roomNo;
}