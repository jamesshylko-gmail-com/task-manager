package com.example.taskmanager.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Details about the User for login")
public class AuthenticationRequestDto {

    @Schema(description = "Username", example = "Tom Cruz")
    private String username;
    @Schema(description = "Open password", example = "I'm a superstar")
    private String password;
}
