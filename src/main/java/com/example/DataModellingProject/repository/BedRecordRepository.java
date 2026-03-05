package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.BedRecord;
import com.example.DataModellingProject.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BedRecordRepository extends JpaRepository<BedRecord, Integer>, JpaSpecificationExecutor<BedRecord> {
    List<BedRecord> findByBed_BedNoIn(List<Integer> bedNos);
    List<BedRecord> findByPatient_PatientIdIn(List<Integer> patientIds);
    List<BedRecord> findByNurse_NurseIdIn(List<Integer> nurseIds);
    List<BedRecord> findByHelper_HelperIdIn(List<Integer> helperIds);

    List<BedRecord> findByBed_BedNo(Integer bedNo);
    List<BedRecord> findByPatient_PatientId(Integer patientId);
    List<BedRecord> findByNurse_NurseId(Integer nurseId);
    List<BedRecord> findByHelper_HelperId(Integer helperId);

    List<BedRecord> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);
    List<BedRecord> findByDischargeDateBetween(LocalDate startDate, LocalDate endDate);
    List<BedRecord> findByAdmissionDateAfter(LocalDate date);
    List<BedRecord> findByDischargeDateBefore(LocalDate date);

    List<BedRecord> findByAmountGreaterThanEqual(Integer amount);
    List<BedRecord> findByAmountLessThanEqual(Integer amount);
    List<BedRecord> findByAmountBetween(Integer minAmount, Integer maxAmount);
}