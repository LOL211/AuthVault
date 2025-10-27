package org.kush.vaultyauth.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(
        @NotBlank
        String username,
        @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not in proper format")
        String email,
        @NotBlank
        String password,
        @NotBlank
        String clientId
){}
