package org.kush.vaultyauth.database.model;

import java.util.Set;
import java.util.UUID;

public record ClientDto(
        UUID clientId,
        Set<String> scopes,
        String clientName
){}
