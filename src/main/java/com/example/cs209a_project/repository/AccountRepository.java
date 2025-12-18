package com.example.cs209a_project.repository;

import com.example.cs209a_project.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
