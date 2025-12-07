package org.kush.vaultyauth.controller.dto;

public record OAuthTokenResponse(
        String access_token,
        String token_type,
        long expires_in
){}
