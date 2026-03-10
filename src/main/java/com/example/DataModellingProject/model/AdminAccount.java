package com.example.DataModellingProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AdminAccount")
@Getter
@Setter
@NoArgsConstructor
public class AdminAccount {

    @Id
    @Column(name = "admin_Id")
    private Integer adminId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;
}