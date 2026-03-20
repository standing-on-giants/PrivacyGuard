package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("BedRecord")
public class BedRecordSearchResponse {

    @PrivacyField(column = "admission_Id")
    private Integer admissionId;

    @PrivacyField(column = "admission_Date")
    private LocalDate admissionDate;

    @PrivacyField(column = "discharge_Date")
    private LocalDate dischargeDate;

    @PrivacyField(column = "amount")
    private Integer amount;

    @PrivacyField(table = "Bed", column = "bed_No")
    private Integer bedNo;

    @PrivacyField(table = "Patient", column = "FName")
    private String patientFirstName;

    @PrivacyField(table = "Patient", column = "LName")
    private String patientLastName;

    @PrivacyField(table = "Nurse", column = "FName")
    private String nurseFirstName;

    @PrivacyField(table = "Nurse", column = "LName")
    private String nurseLastName;

    @PrivacyField(table = "Helper", column = "FName")
    private String helperFirstName;

    @PrivacyField(table = "Helper", column = "LName")
    private String helperLastName;
}