package org.kush.vaultyauth.config;

import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.model.UserDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

public class UserToken extends AbstractAuthenticationToken {

    private UserDto user;
    public UserToken(User user, Authentication token) {
        super(token.getAuthorities());
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
