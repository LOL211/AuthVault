package org.kush.vaultyauth.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserToken extends AbstractAuthenticationToken {

    private final UUID userId;

    public UserToken(UUID id, Set<String> scopes) {
        super(scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.userId = id;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
