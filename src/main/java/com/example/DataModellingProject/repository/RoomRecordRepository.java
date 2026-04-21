package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.RoomRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRecordRepository extends JpaRepository<RoomRecord, Integer>, JpaSpecificationExecutor<RoomRecord> {
}