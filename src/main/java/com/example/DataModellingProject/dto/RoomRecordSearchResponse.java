package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("RoomRecord")
public class RoomRecordSearchResponse {
    @PrivacyField(table = "RoomRecord", column = "record_Id")
    private Integer recordId;
    
    @PrivacyField(table = "RoomRecord", column = "admission_Date")
    private LocalDate admissionDate;
    
    @PrivacyField(table = "RoomRecord", column = "discharge_Date")
    private LocalDate dischargeDate;
    
    @PrivacyField(table = "Room", column = "room_No")
    private Integer roomNo;
    
    @PrivacyField(table = "Patient", column = "FName")
    private String patientFirstName;
    
    @PrivacyField(table = "Patient", column = "LName")
    private String patientLastName;
    
    @PrivacyField(table = "Nurse", column = "FName")
    private String nurseFirstName;
    
    @PrivacyField(table = "Nurse", column = "LName")
    private String nurseLastName;
}