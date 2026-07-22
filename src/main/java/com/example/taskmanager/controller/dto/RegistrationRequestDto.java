package com.example.taskmanager.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
}
