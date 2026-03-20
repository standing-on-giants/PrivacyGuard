package com.example.DataModellingProject.dto;

import com.example.DataModellingProject.privacy.annotation.PrivacyField;
import com.example.DataModellingProject.privacy.annotation.PrivacyTable;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@PrivacyTable("Appointment") // Default table for this DTO
public class AppointmentSearchResponse {

    @PrivacyField(column = "appointment_Id")
    private Integer appointmentId;

    @PrivacyField(column = "appointment_Date")
    private LocalDate appointmentDate;

    @PrivacyField(column = "mode_of_appointment")
    private String modeOfAppointment;

    @PrivacyField(column = "appointment_status")
    private String appointmentStatus;

    @PrivacyField(column = "payment_amount")
    private String paymentAmount;

    // Explicitly overrides to use "Patient" table
    @PrivacyField(table = "Patient", column = "FName")
    private String patientFirstName;

    @PrivacyField(table = "Patient", column = "LName")
    private String patientLastName;

    // Explicitly overrides to use "Doctor" table
    @PrivacyField(table = "Doctor", column = "FName")
    private String doctorFirstName;

    @PrivacyField(table = "Doctor", column = "LName")
    private String doctorLastName;
}