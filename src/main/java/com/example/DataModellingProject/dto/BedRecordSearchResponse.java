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
    private Integer admissionId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private Integer amount;
    private Integer bedNo;
    private String patientFirstName;
    private String patientLastName;
    private String nurseFirstName;
    private String nurseLastName;
    private String helperFirstName;
    private String helperLastName;
}