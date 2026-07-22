package com.example.taskmanager.service;

import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.model.User;

public interface UserService {

    User findByUsername(String username);

    User save(RegistrationRequestDto user);

    User getCurrentUser();
}
