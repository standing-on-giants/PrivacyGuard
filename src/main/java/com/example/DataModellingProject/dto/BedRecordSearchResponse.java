package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class BedRecordSearchResponse {
    private Integer recordId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private Integer amount;
    private Integer bedNo;
    private String patientName;
    private String nurseName;
}