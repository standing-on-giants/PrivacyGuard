package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Department")
@Getter
@Setter
@NoArgsConstructor
public class Department {
    @Id
    @Column(name="dept_Id")
    private Integer deptId;

    @Column(name="dept_Name", length = 100)
    private String deptName;
}
