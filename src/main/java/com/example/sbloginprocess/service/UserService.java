package com.example.sbloginprocess.service;

import com.example.sbloginprocess.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registerUser(String username, String password);

    boolean isUsernameTaken(String username);
}
