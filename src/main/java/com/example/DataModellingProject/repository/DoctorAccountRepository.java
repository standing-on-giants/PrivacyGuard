package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.DoctorAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorAccountRepository extends JpaRepository<DoctorAccount, Integer> {
    Optional<DoctorAccount> findByEmail(String email);
}