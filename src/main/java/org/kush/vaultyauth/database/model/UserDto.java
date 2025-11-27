package org.kush.vaultyauth.database.model;

public record UserDto (
        String email,
        String password,
        String username
){}
