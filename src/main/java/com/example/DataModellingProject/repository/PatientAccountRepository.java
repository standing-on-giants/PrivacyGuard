package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.PatientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientAccountRepository extends JpaRepository<PatientAccount, Integer> {
    Optional<PatientAccount> findByEmail(String email);
}