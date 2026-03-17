package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@PrivacyTable("StaffShift")
public class StaffShiftSearchResponse {
    private Integer shiftId;
    private LocalDate shiftDate;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private String doctorFirstName;
    private String doctorLastName;
    private String nurseFirstName;
    private String nurseLastName;
    private String helperFirstName;
    private String helperLastName;
}