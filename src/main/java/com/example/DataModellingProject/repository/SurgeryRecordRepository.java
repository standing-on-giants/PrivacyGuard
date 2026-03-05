package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.SurgeryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SurgeryRecordRepository extends JpaRepository<SurgeryRecord, Integer>, JpaSpecificationExecutor<SurgeryRecord> {

    List<SurgeryRecord> findByPatient_PatientIdIn(List<Integer> patientIds);
    List<SurgeryRecord> findBySurgeon_DoctIdIn(List<Integer> surgeonIds);
    List<SurgeryRecord> findByRoom_RoomNoIn(List<Integer> roomNos);
    List<SurgeryRecord> findByNurse_NurseIdIn(List<Integer> nurseIds);
    List<SurgeryRecord> findByHelper_HelperIdIn(List<Integer> helperIds);
    List<SurgeryRecord> findBySurgeryTypeIn(List<String> surgeryTypes);

    List<SurgeryRecord> findByPatient_PatientId(Integer patientId);
    List<SurgeryRecord> findBySurgeon_DoctId(Integer surgeonId);
    List<SurgeryRecord> findByRoom_RoomNo(Integer roomNo);
    List<SurgeryRecord> findByNurse_NurseId(Integer nurseId);
    List<SurgeryRecord> findByHelper_HelperId(Integer helperId);
    List<SurgeryRecord> findBySurgeryType(String surgeryType);

    List<SurgeryRecord> findBySurgeryDate(LocalDate surgeryDate);
    List<SurgeryRecord> findBySurgeryDateBetween(LocalDate startDate, LocalDate endDate);
    List<SurgeryRecord> findBySurgeryDateAfter(LocalDate date);
    List<SurgeryRecord> findBySurgeryDateBefore(LocalDate date);

    List<SurgeryRecord> findByStartTimeBetween(LocalTime start, LocalTime end);
    List<SurgeryRecord> findByEndTimeBetween(LocalTime start, LocalTime end);

    List<SurgeryRecord> findBySurgeryTypeContainingIgnoreCase(String keyword);
}