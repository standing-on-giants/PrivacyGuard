package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.HelperAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HelperAccountRepository extends JpaRepository<HelperAccount, Integer> {
    Optional<HelperAccount> findByEmail(String email);
}