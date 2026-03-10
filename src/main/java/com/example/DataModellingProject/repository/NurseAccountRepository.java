package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.NurseAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NurseAccountRepository extends JpaRepository<NurseAccount, Integer> {
    Optional<NurseAccount> findByEmail(String email);
}