package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class SurgeryRecordSearchRequest {
    private List<Integer> patientIds;
    private List<Integer> surgeonIds;
    private List<Integer> roomNos;
    private List<Integer> nurseIds;
    private List<Integer> helperIds;
    private List<String> surgeryTypes;
    private String surgeryTypeKeyword;
    private LocalDate surgeryDateStart;
    private LocalDate surgeryDateEnd;
    private LocalTime startTimeStart;
    private LocalTime startTimeEnd;
    private LocalTime endTimeStart;
    private LocalTime endTimeEnd;
}