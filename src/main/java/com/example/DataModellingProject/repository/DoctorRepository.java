package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer>, JpaSpecificationExecutor<Doctor> {
    List<Doctor> findByfNameIn(List<String> fNames);
    List<Doctor> findBylNameIn(List<String> lNames);
    List<Doctor> findByGenderIn(List<String> genders);
    List<Doctor> findBySurgeonTypeIn(List<String> surgeonTypes);
    List<Doctor> findByDepartment_DeptNameIn(List<String> deptNames);
    List<Doctor> findByOffice_RoomNoIn(List<Integer> roomNos);

    List<Doctor> findByfName(String fName);
    List<Doctor> findBylName(String lName);
    List<Doctor> findByGender(String gender);
    List<Doctor> findBySurgeonType(String surgeonType);
    List<Doctor> findByDepartment_DeptName(String deptName);
    List<Doctor> findByOffice_RoomNo(Integer roomNo);
}