package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RoomRecordSearchResponse {
    private Integer recordId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private Integer roomNo;
    private String patientName;
    private String nurseName;
}