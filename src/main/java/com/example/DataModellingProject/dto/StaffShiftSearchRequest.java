package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class StaffShiftSearchRequest {
    private List<Integer> doctorIds;
    private List<Integer> nurseIds;
    private List<Integer> helperIds;
    private List<LocalDate> exactShiftDates;
    private LocalDate shiftDateStart;
    private LocalDate shiftDateEnd;
    private LocalTime shiftStartStart;
    private LocalTime shiftStartEnd;
    private LocalTime shiftEndStart;
    private LocalTime shiftEndEnd;
}