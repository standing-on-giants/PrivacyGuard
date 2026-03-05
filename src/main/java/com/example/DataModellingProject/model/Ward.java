package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Ward")
@Getter
@Setter
@NoArgsConstructor
public class Ward {
    @Id
    @Column(name="ward_No")
    private Integer wardNo;

    @Column(name="ward_Name", length = 100)
    private String wardName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dept_Id")
    private Department department;
}