package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@PrivacyTable("SurgeryRecord")
public class SurgeryRecordSearchResponse {
    private Integer surgeryId;
    private String surgeryType;
    private LocalDate surgeryDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String patientFirstName;
    private String patientLastName;
    private String surgeonFirstName;
    private String surgeonLastName;
    private Integer roomNo;
}