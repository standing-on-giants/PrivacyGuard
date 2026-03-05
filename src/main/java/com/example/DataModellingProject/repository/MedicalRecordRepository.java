package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer>, JpaSpecificationExecutor<MedicalRecord> {
    List<MedicalRecord> findByDoctor_DoctIdIn(List<Integer> doctorIds);
    List<MedicalRecord> findByPatient_PatientIdIn(List<Integer> patientIds);


    List<MedicalRecord> findByDoctor_DoctId(Integer doctId);
    List<MedicalRecord> findByPatient_PatientId(Integer patientId);

    List<MedicalRecord> findByVisitDate(LocalDate visitDate);
    List<MedicalRecord> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);
    List<MedicalRecord> findByVisitDateAfter(LocalDate date);

    List<MedicalRecord> findByNextVisit(LocalDate nextVisit);
    List<MedicalRecord> findByNextVisitBetween(LocalDate startDate, LocalDate endDate);
}