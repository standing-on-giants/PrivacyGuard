package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PatientSearchRequest {
    private List<Integer> patientIds;
    private List<String> fNames;
    private List<String> lNames;
    private List<String> genders;
    private LocalDate dobStart;
    private LocalDate dobEnd;
}