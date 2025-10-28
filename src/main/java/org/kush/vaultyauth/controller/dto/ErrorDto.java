package org.kush.vaultyauth.controller.dto;

public record ErrorDto(
        String error_message,
        String errorType
){}
