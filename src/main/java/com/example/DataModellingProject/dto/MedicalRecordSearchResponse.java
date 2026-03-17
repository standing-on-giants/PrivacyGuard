package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("MedicalRecord")
public class MedicalRecordSearchResponse {
    private Integer recordId;
    private LocalDate visitDate;
    private LocalDate nextVisit;
    private String patientFirstName;
    private String patientLastName;
    private String doctorFirstName;
    private String doctorLastName;
}