package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.Helper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface HelperRepository extends JpaRepository<Helper, Integer>, JpaSpecificationExecutor<Helper> {
}