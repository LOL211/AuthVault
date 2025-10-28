package org.kush.vaultyauth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotBlank(message = "Password should not be empty")
        String password
){}
