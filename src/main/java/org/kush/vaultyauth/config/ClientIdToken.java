package org.kush.vaultyauth.config;

import org.kush.vaultyauth.database.model.ClientDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientIdToken extends AbstractAuthenticationToken
{
    private final ClientDto clientDto;

    public ClientIdToken(ClientDto client)
    {
        super(Arrays.stream(client.scopes().split(" ")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.clientDto = client;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials()
    {
        return clientDto.clientId();
    }

    @Override
    public Object getPrincipal()
    {
        return clientDto;
    }

    @Override
    public boolean implies(Subject subject)
    {
        return super.implies(subject);
    }
}
