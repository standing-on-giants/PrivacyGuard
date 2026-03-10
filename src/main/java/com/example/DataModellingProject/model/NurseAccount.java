package com.example.DataModellingProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NurseAccount")
@Getter
@Setter
@NoArgsConstructor
public class NurseAccount {

    @Id
    @Column(name = "nurse_Id")
    private Integer nurseId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "nurse_Id")
    private Nurse nurse;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;
}