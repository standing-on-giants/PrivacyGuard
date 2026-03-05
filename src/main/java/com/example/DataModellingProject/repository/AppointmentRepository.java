package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Appointment;
import com.example.DataModellingProject.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatient_PatientIdIn(List<Integer> patientIds);
    List<Appointment> findByDoctor_DoctIdIn(List<Integer> doctorIds);
    List<Appointment> findByModeOfAppointmentIn(List<String> modesOfAppointment);
    List<Appointment> findByAppointmentStatusIn(List<String> appointmentStatuses);

    List<Appointment> findByPatient_PatientId(Integer patientId);
    List<Appointment> findByDoctor_DoctId(Integer doctId);
    List<Appointment> findByModeOfAppointment(String modeOfAppointment);
    List<Appointment> findByAppointmentStatus(String appointmentStatus);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
    List<Appointment> findByAppointmentDateAfter(LocalDate date);
    List<Appointment> findByAppointmentDateBefore(LocalDate date);
}