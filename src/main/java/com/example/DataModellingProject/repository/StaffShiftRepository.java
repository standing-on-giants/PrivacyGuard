package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.model.StaffShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface StaffShiftRepository extends JpaRepository<StaffShift, Integer>, JpaSpecificationExecutor<StaffShift> {
    List<StaffShift> findByDoctor_DoctIdIn(List<Integer> doctorIds);
    List<StaffShift> findByNurse_NurseIdIn(List<Integer> nurseIds);
    List<StaffShift> findByHelper_HelperIdIn(List<Integer> helperIds);
    List<StaffShift> findByShiftDateIn(List<LocalDate> shiftDates);

    List<StaffShift> findByDoctor_DoctId(Integer doctId);
    List<StaffShift> findByNurse_NurseId(Integer nurseId);
    List<StaffShift> findByHelper_HelperId(Integer helperId);
    List<StaffShift> findByShiftDate(LocalDate shiftDate);
    List<StaffShift> findByShiftStart(LocalTime shiftStart);
    List<StaffShift> findByShiftEnd(LocalTime shiftEnd);

    List<StaffShift> findByShiftDateBetween(LocalDate startDate, LocalDate endDate);
    List<StaffShift> findByShiftDateAfter(LocalDate date);
    List<StaffShift> findByShiftDateBefore(LocalDate date);

    List<StaffShift> findByShiftStartBetween(LocalTime startTime, LocalTime endTime);
    List<StaffShift> findByShiftEndBetween(LocalTime startTime, LocalTime endTime);
}