package com.example.sbloginprocess.repository;

import com.example.sbloginprocess.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Add custom query methods if needed
}
