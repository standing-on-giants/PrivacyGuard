package com.example.DataModellingProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HelperAccount")
@Getter
@Setter
@NoArgsConstructor
public class HelperAccount {

    @Id
    @Column(name = "helper_Id")
    private Integer helperId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "helper_Id")
    private Helper helper;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;
}