package org.kush.vaultyauth.config;

import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.model.UserDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class UserToken extends AbstractAuthenticationToken {

    private UserDto user;
    public UserToken(User user) {
        super(Collections.emptyList());
        this.user = new UserDto(user.getEmail(), user.getPassword(), user.getUsername());
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return user.password();
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
