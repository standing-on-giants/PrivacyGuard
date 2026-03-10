package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Doctor")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {
    @Id
    @Column(name="doct_Id")
    private Integer doctId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dept_Id")
    private Department department;

    @Column(name="FName", length = 100)
    private String fName;

    @Column(name="LName", length = 100)
    private String lName;

    @Column(name="Gender", length = 1)
    private String gender;

    @Column(name="contact_No", length = 100)
    private String contactNo;

    @Column(name="surgeon_Type", length = 100)
    private String surgeonType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="office_No")
    private Room office;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorAccount account;
}
