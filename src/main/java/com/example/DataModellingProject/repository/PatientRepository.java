package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>, JpaSpecificationExecutor<Patient> {
    List<Patient> findByPatientIdIn(List<Integer> patientIds);
    List<Patient> findByfNameIn(List<String> fNames);
    List<Patient> findBylNameIn(List<String> lNames);
    List<Patient> findByGenderIn(List<String> genders);

    List<Patient> findByPatientId(Integer patientId);
    List<Patient> findByfName(String fName);
    List<Patient> findBylName(String lName);
    List<Patient> findByGender(String gender);

    List<Patient> findByDateOfBirth(LocalDate dateOfBirth);
    List<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
    List<Patient> findByDateOfBirthAfter(LocalDate date);
    List<Patient> findByDateOfBirthBefore(LocalDate date);
}