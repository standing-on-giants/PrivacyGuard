package com.example.DataModellingProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PatientAccount")
@Getter
@Setter
@NoArgsConstructor
public class PatientAccount {

    @Id
    @Column(name = "patient_Id")
    private Integer patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_Id")
    private Patient patient;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;
}