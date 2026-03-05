package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name="BedRecord")
@Getter
@Setter
@NoArgsConstructor
public class BedRecord {
    @Id
    @Column(name="admission_Id")
    private Integer admissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bed_No")
    private Bed bed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_Id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nurse_Id")
    private Nurse nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="helper_Id")
    private Helper helper;

    @Column(name="admission_Date")
    private LocalDate admissionDate;

    @Column(name="discharge_Date")
    private LocalDate dischargeDate;

    @Column(name="amount")
    private Integer amount;

    @Column(name="mode_of_payment", length = 50)
    private String modeOfPayment;
}
