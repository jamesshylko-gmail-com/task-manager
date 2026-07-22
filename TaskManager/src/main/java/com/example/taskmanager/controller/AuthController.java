package com.example.taskmanager.controller;

import com.example.taskmanager.controller.dto.AuthenticationRequestDto;
import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.User;
import com.example.taskmanager.security.JwtTokenProvider;
import com.example.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDto dto) {
        User user = userService.save(dto);
        if (nonNull(user)) {
            return login(userMapper.mapToAuthenticationRequestDto(dto));
        }
        throw new IllegalStateException("Registration failed");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        return login(request);
    }

    private ResponseEntity<?> login(AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
            User user = userService.findByUsername(request.getUsername());
            if (isNull(user)) {
                throw new UsernameNotFoundException("User doesn't exists");
            }
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
            return ResponseEntity.ok(Map.ofEntries(
                    entry("username", user.getUsername()),
                    entry("token", token)
            ));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid login or password", FORBIDDEN);
        }
    }
}
