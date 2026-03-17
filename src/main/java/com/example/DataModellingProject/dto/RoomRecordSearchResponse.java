package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("RoomRecord")
public class RoomRecordSearchResponse {
    private Integer recordId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private Integer roomNo;
    private String patientFirstName;
    private String patientLastName;
    private String nurseFirstName;
    private String nurseLastName;
}