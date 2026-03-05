package com.example.DataModellingProject.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AppointmentSearchRequest {
    private List<Integer> patientIds;
    private List<Integer> doctorIds;
    private List<String> modesOfAppointment;
    private List<String> appointmentStatuses;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}