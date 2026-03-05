package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SurgeryRecordSearchResponse {
    private Integer surgeryId;
    private String surgeryType;
    private LocalDate surgeryDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String patientName;
    private String surgeonName;
    private Integer roomNo;
}