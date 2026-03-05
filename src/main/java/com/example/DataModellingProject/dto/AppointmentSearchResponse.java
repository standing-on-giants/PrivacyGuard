package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AppointmentSearchResponse {
    private Integer appointmentId;
    private LocalDate appointmentDate;
    private String modeOfAppointment;
    private String appointmentStatus;
    private String patientName;
    private String doctorName;
}