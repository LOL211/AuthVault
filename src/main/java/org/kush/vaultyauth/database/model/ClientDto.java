package org.kush.vaultyauth.database.model;

public record ClientDto(
        String clientId,
        String scopes,
        String clientName
){}
