package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Helper")
public class HelperSearchResponse {
    @PrivacyField(table = "Helper", column = "helper_Id")
    private Integer helperId;
    
    @PrivacyField(table = "Helper", column = "FName")
    private String fName;
    
    @PrivacyField(table = "Helper", column = "LName")
    private String lName;
    
    @PrivacyField(table = "Helper", column = "Gender")
    private String gender;
    
    @PrivacyField(table = "Helper", column = "contact_No")
    private String contactNo;
    
    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;
}