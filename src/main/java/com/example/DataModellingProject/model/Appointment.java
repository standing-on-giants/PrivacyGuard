package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name="Appointment")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @Column(name="appointment_Id")
    private Integer appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_Id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doct_Id")
    private Doctor doctor;

    @Column(name="reason", length = 100)
    private String reason;

    @Column(name="appointment_Date")
    private LocalDate appointmentDate;

    @Column(name="payment_amount")
    private Integer paymentAmount;

    @Column(name="mode_of_payment", length = 100)
    private String modeOfPayment;

    @Column(name="mode_of_appointment", length = 100)
    private String modeOfAppointment;

    @Column(name="appointment_status", length = 100)
    private String appointmentStatus;
}
