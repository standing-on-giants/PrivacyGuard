package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Doctor;
import com.example.DataModellingProject.model.Helper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperRepository extends JpaRepository<Helper, Integer>, JpaSpecificationExecutor<Helper> {

    List<Helper> findByHelperIdIn(List<Integer> helperIds);
    List<Helper> findByDepartment_DeptNameIn(List<String> deptNames);
    List<Helper> findByfNameIn(List<String> fNames);
    List<Helper> findBylNameIn(List<String> lNames);
    List<Helper> findByGenderIn(List<String> genders);

    List<Helper> findByHelperId(Integer helperId);
    List<Helper> findByDepartment_DeptName(String deptName);
    List<Helper> findByfName(String fName);
    List<Helper> findBylName(String lName);
    List<Helper> findByGender(String gender);
}