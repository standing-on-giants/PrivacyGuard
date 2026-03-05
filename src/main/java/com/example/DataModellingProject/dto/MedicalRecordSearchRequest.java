package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MedicalRecordSearchRequest {
    private List<Integer> doctorIds;
    private List<Integer> patientIds;
    private LocalDate visitDateStart;
    private LocalDate visitDateEnd;
    private LocalDate nextVisitStart;
    private LocalDate nextVisitEnd;
}