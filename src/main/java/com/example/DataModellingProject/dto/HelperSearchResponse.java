package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Helper")
public class HelperSearchResponse {

    @PrivacyField(column = "helper_Id")
    private Integer helperId;

    @PrivacyField(column = "FName")
    private String fName;

    @PrivacyField(column = "LName")
    private String lName;

    @PrivacyField(column = "Gender")
    private String gender;

    @PrivacyField(column = "contact_No")
    private String contactNo;

    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;
}