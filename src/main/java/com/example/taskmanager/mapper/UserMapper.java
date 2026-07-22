package com.example.taskmanager.mapper;

import com.example.taskmanager.controller.dto.AuthenticationRequestDto;
import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        imports = { Role.class }
)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword()))")
    @Mapping(target = "role", expression = "java(Role.USER)")
    public abstract User mapToUser(RegistrationRequestDto dto);

    public abstract AuthenticationRequestDto mapToAuthenticationRequestDto(RegistrationRequestDto dto);
}
