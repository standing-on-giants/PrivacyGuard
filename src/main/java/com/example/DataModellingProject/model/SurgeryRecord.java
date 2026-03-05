package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="SurgeryRecord")
@Getter
@Setter
@NoArgsConstructor
public class SurgeryRecord {
    @Id
    @Column(name="surgery_Id")
    private Integer surgeryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_Id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="surgeon_Id")
    private Doctor surgeon;

    @Column(name="surgery_Type", length = 100)
    private String surgeryType;

    @Column(name="surgery_Date")
    private LocalDate surgeryDate;

    @Column(name="start_Time")
    private LocalTime startTime;

    @Column(name="end_Time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_no")
    private Room room;

    @Column(name="notes", length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nurse_Id")
    private Nurse nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="helper_Id")
    private Helper helper;
}
