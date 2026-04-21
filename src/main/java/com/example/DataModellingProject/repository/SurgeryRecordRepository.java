package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.SurgeryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SurgeryRecordRepository extends JpaRepository<SurgeryRecord, Integer>, JpaSpecificationExecutor<SurgeryRecord> {
}