package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Nurse")
public class NurseSearchResponse {

    @PrivacyField(column = "nurse_Id")
    private Integer nurseId;

    @PrivacyField(column = "FName")
    private String fName;

    @PrivacyField(column = "LName")
    private String lName;

    @PrivacyField(column = "Gender")
    private String gender;

    // FIX: Matched the spelling mistake 'conatct_No' from your DDL exactly!
    @PrivacyField(column = "conatct_No")
    private String contactNo;

    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;
}