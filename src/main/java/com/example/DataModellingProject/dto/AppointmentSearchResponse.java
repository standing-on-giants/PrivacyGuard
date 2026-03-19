package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("Appointment")
public class AppointmentSearchResponse {
    private Integer appointmentId;
    private LocalDate appointmentDate;
    private String modeOfAppointment;
    private String appointmentStatus;
    
    // Explicitly mapping Java fields to the DB table/columns used in your XML rules
    @PrivacyField(table = "patients", column = "firstname")
    private String patientFirstName;
    
    @PrivacyField(table = "patients", column = "lastname")
    private String patientLastName;
    
    @PrivacyField(table = "doctors", column = "firstname")
    private String doctorFirstName;
    
    @PrivacyField(table = "doctors", column = "lastname")
    private String doctorLastName;
}