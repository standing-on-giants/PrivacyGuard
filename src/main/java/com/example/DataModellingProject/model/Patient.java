package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Entity
@Table(name="Patient")
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @Column(name="patient_Id")
    private Integer patientId;

    @Column(name="FName", length = 100)
    private String fName;

    @Column(name="LName", length = 100)
    private String lName;

    @Column(name="Gender", length = 1)
    private String gender;

    @Column(name="Date_Of_Birth")
    private LocalDate dateOfBirth;

    @Column(name="contact_No", length = 100)
    private String contactNo;

    @Column(name="pt_Address", length = 100)
    private String ptAddress;
}
