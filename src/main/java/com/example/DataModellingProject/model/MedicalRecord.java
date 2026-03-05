package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="MedicalRecord")
@Getter
@Setter
@NoArgsConstructor
public class MedicalRecord {
    @Id
    @Column(name="record_Id")
    private Integer recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doct_Id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patient_Id")
    private Patient patient;

    @Column(name="visit_Date")
    private LocalDate visitDate;

    @Column(name="curr_Weight", precision = 10, scale = 2)
    private BigDecimal currWeight;

    @Column(name="curr_height", precision = 10, scale = 2)
    private BigDecimal currHeight;

    @Column(name="curr_Blood_Pressure", length = 100)
    private String currBloodPressure;

    @Column(name="curr_Temp_F", precision = 10, scale = 2)
    private BigDecimal currTempF;

    @Column(name="diagnosis", length = 500)
    private String diagnosis;

    @Column(name="treatment", length = 100)
    private String treatment;

    @Column(name="next_Visit")
    private LocalDate nextVisit;
}
