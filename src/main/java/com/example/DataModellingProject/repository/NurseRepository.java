package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer>, JpaSpecificationExecutor<Nurse> {
    List<Nurse> findByNurseIdIn(List<Integer> nurseIds);
    List<Nurse> findByDepartment_DeptNameIn(List<String> deptNames);
    List<Nurse> findByfNameIn(List<String> fNames);
    List<Nurse> findBylNameIn(List<String> lNames);
    List<Nurse> findByGenderIn(List<String> genders);

    List<Nurse> findByNurseId(Integer nurseId);
    List<Nurse> findByDepartment_DeptName(String deptName);
    List<Nurse> findByfName(String fName);
    List<Nurse> findBylName(String lName);
    List<Nurse> findByGender(String gender);
}