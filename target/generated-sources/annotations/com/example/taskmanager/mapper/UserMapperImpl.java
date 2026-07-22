package com.example.taskmanager.mapper;

import com.example.taskmanager.controller.dto.AuthenticationRequestDto;
import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-22T14:27:26+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User mapToUser(RegistrationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( dto.getUsername() );
        user.email( dto.getEmail() );

        user.password( passwordEncoder.encode(dto.getPassword()) );
        user.role( Role.USER );

        return user.build();
    }

    @Override
    public AuthenticationRequestDto mapToAuthenticationRequestDto(RegistrationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        AuthenticationRequestDto.AuthenticationRequestDtoBuilder authenticationRequestDto = AuthenticationRequestDto.builder();

        authenticationRequestDto.username( dto.getUsername() );
        authenticationRequestDto.password( dto.getPassword() );

        return authenticationRequestDto.build();
    }
}
