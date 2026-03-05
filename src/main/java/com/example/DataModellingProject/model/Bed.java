package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Bed")
@Getter
@Setter
@NoArgsConstructor
public class Bed {
    @Id
    @Column(name="bed_No")
    private Integer bedNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ward_No")
    private Ward ward;
}
