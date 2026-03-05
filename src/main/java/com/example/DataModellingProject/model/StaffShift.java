package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="StaffShift")
@Getter
@Setter
@NoArgsConstructor
public class StaffShift {
    @Id
    @Column(name="shift_Id")
    private Integer shiftId;

    @ManyToOne
    @JoinColumn(name="doct_Id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name="nurse_Id")
    private Nurse nurse;

    @ManyToOne
    @JoinColumn(name="helper_Id")
    private Helper helper;

    @Column(name="shift_Date")
    private LocalDate shiftDate;

    @Column(name="shift_Start")
    private LocalTime shiftStart;

    @Column(name="shift_End")
    private LocalTime shiftEnd;
}