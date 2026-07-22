package com.example.taskmanager.mapper;

import com.example.taskmanager.controller.dto.AuthenticationRequestDto;
import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {
        UserMapperImpl.class
})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void mapToUser() {
        // Given
        RegistrationRequestDto dto = RegistrationRequestDto.builder()
                .username("user")
                .email("user@gmail.com")
                .password("password")
                .build();

        // Mocking the behavior of the autowired interface
        when(passwordEncoder.encode(anyString())).thenAnswer(invocationOnMock ->
                new BCryptPasswordEncoder().encode(invocationOnMock.getArgument(0)));

        // When
        User result = userMapper.mapToUser(dto);

        // Then
        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals("user@gmail.com", result.getEmail());
        assertNotEquals("password", result.getPassword()); //encrypted
        assertEquals("USER", result.getRole().name());
    }

    @Test
    void mapToAuthenticationRequestDto() {
        // Given
        RegistrationRequestDto dto = RegistrationRequestDto.builder()
                .username("user")
                .email("user@gmail.com")
                .password("password")
                .build();

        // When
        AuthenticationRequestDto result = userMapper.mapToAuthenticationRequestDto(dto);

        // Then
        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals("password", result.getPassword()); //encrypted
    }
}