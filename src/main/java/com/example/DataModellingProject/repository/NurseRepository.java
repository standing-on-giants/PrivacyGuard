package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer>, JpaSpecificationExecutor<Nurse> {
}