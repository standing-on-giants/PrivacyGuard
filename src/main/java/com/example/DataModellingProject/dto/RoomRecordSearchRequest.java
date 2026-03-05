package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RoomRecordSearchRequest {
    private List<Integer> roomNos;
    private List<Integer> patientIds;
    private List<Integer> nurseIds;
    private List<Integer> helperIds;
    private LocalDate admissionDateStart;
    private LocalDate admissionDateEnd;
    private LocalDate dischargeDateStart;
    private LocalDate dischargeDateEnd;
}