package com.example.taskmanager.service.impl;

import com.example.taskmanager.controller.dto.RegistrationRequestDto;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.User;
import com.example.taskmanager.perository.UserRepository;
import com.example.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(RegistrationRequestDto user) {
        return userRepository.save(userMapper.mapToUser(user));
    }

    @Override
    public User getCurrentUser() {
        UsernamePasswordAuthenticationToken authentication =  (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        if (nonNull(authentication)) {
            org.springframework.security.core.userdetails.User authUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            if (nonNull(authUser)) {
                return userRepository.findByUsername(authUser.getUsername());
            }
        }
        return null;
    }
}
