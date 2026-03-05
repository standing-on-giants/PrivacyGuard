package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class MedicalRecordSearchResponse {
    private Integer recordId;
    private LocalDate visitDate;
    private LocalDate nextVisit;
    private String patientName;
    private String doctorName;
}