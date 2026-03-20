package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("MedicalRecord")
public class MedicalRecordSearchResponse {

    @PrivacyField(column = "record_Id")
    private Integer recordId;

    @PrivacyField(column = "visit_Date")
    private LocalDate visitDate;

    @PrivacyField(column = "next_Visit")
    private LocalDate nextVisit;

    @PrivacyField(column = "diagnosis")
    private String diagnosis;

    @PrivacyField(table = "Patient", column = "FName")
    private String patientFirstName;

    @PrivacyField(table = "Patient", column = "LName")
    private String patientLastName;

    @PrivacyField(table = "Doctor", column = "FName")
    private String doctorFirstName;

    @PrivacyField(table = "Doctor", column = "LName")
    private String doctorLastName;
}