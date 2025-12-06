package org.kush.vaultyauth.database.model;

import java.util.UUID;

public record ClientDto(
        UUID clientId,
        String scopes,
        String clientName
){}
