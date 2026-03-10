package com.example.DataModellingProject.repository;

import com.example.DataModellingProject.model.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, Integer> {
    Optional<AdminAccount> findByEmail(String email);
}