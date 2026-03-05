package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.model.RoomRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRecordRepository extends JpaRepository<RoomRecord, Integer>, JpaSpecificationExecutor<RoomRecord> {
    List<RoomRecord> findByRoom_RoomNoIn(List<Integer> roomNos);
    List<RoomRecord> findByPatient_PatientIdIn(List<Integer> patientIds);
    List<RoomRecord> findByNurse_NurseIdIn(List<Integer> nurseIds);
    List<RoomRecord> findByHelper_HelperIdIn(List<Integer> helperIds);

    List<RoomRecord> findByRoom_RoomNo(Integer roomNo);
    List<RoomRecord> findByPatient_PatientId(Integer patientId);
    List<RoomRecord> findByNurse_NurseId(Integer nurseId);
    List<RoomRecord> findByHelper_HelperId(Integer helperId);

    List<RoomRecord> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);
    List<RoomRecord> findByDischargeDateBetween(LocalDate startDate, LocalDate endDate);
    List<RoomRecord> findByAdmissionDateAfter(LocalDate date);
    List<RoomRecord> findByDischargeDateBefore(LocalDate date);
}