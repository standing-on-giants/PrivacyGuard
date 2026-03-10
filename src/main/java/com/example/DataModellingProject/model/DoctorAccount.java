package com.example.DataModellingProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DoctorAccount")
@Getter
@Setter
@NoArgsConstructor
public class DoctorAccount {

    @Id
    @Column(name = "doctor_Id")
    private Integer doctorId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "doctor_Id")
    private Doctor doctor;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;
}