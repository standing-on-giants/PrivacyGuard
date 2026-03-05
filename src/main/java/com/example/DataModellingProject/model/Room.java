package com.example.DataModellingProject.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Room")
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id
    @Column(name="room_No")
    private Integer roomNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dept_Id")
    private Department department;

    @Column(name="room_Type", length = 100)
    private String roomType;
}