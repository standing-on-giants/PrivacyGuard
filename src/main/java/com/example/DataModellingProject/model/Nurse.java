package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Nurse")
@Getter
@Setter
@NoArgsConstructor
public class Nurse {
    @Id
    @Column(name="nurse_Id")
    private Integer nurseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dept_Id")
    private Department department;

    @Column(name="FName", length = 100)
    private String fName;

    @Column(name="LName", length = 100)
    private String lName;

    @Column(name="Gender", length = 1)
    private String gender;

    @Column(name="conatct_No", length = 100)
    private String contactNo;
}