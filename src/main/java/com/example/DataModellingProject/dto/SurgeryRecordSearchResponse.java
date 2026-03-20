package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@PrivacyTable("SurgeryRecord")
public class SurgeryRecordSearchResponse {

    @PrivacyField(column = "surgery_Id")
    private Integer surgeryId;

    @PrivacyField(column = "surgery_Type")
    private String surgeryType;

    @PrivacyField(column = "surgery_Date")
    private LocalDate surgeryDate;

    @PrivacyField(column = "start_Time")
    private LocalTime startTime;

    @PrivacyField(column = "end_Time")
    private LocalTime endTime;

    @PrivacyField(table = "Patient", column = "FName")
    private String patientFirstName;

    @PrivacyField(table = "Patient", column = "LName")
    private String patientLastName;

    @PrivacyField(table = "Doctor", column = "FName")
    private String surgeonFirstName;

    @PrivacyField(table = "Doctor", column = "LName")
    private String surgeonLastName;

    @PrivacyField(table = "Room", column = "room_No")
    private Integer roomNo;
}