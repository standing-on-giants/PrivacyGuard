package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PrivacyTable("Doctor")
public class DoctorSearchResponse {

    @PrivacyField(column = "doct_Id")
    private Integer doctId;

    @PrivacyField(column = "FName")
    private String fName;

    @PrivacyField(column = "LName")
    private String lName;

    @PrivacyField(column = "Gender")
    private String gender;

    @PrivacyField(column = "contact_No")
    private String contactNo;

    @PrivacyField(column = "surgeon_Type")
    private String surgeonType;

    @PrivacyField(table = "Department", column = "dept_Name")
    private String deptName;

    @PrivacyField(table = "Room", column = "room_No")
    private Integer roomNo;
}