package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@PrivacyTable("StaffShift")
public class StaffShiftSearchResponse {
    @PrivacyField(table = "StaffShift", column = "shift_Id")
    private Integer shiftId;
    
    @PrivacyField(table = "StaffShift", column = "shift_Date")
    private LocalDate shiftDate;
    
    @PrivacyField(table = "StaffShift", column = "shift_Start")
    private LocalTime shiftStart;
    
    @PrivacyField(table = "StaffShift", column = "shift_End")
    private LocalTime shiftEnd;
    
    @PrivacyField(table = "Doctor", column = "FName")
    private String doctorFirstName;
    
    @PrivacyField(table = "Doctor", column = "LName")
    private String doctorLastName;
    
    @PrivacyField(table = "Nurse", column = "FName")
    private String nurseFirstName;
    
    @PrivacyField(table = "Nurse", column = "LName")
    private String nurseLastName;
    
    @PrivacyField(table = "Helper", column = "FName")
    private String helperFirstName;
    
    @PrivacyField(table = "Helper", column = "LName")
    private String helperLastName;
}