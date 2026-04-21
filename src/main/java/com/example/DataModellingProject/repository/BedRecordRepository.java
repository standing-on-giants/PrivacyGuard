package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.BedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRecordRepository extends JpaRepository<BedRecord, Integer>, JpaSpecificationExecutor<BedRecord> {
}