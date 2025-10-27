package org.kush.vaultyauth.config;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.ClientDto;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientIdFilter extends OncePerRequestFilter
{
    private final ClientRepository clientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String clientId = request.getHeader("client_id");
        if (StringUtils.isNotBlank(clientId))
        {
            clientRepository.findClientByClientId(clientId).ifPresent(client -> {
                ClientDto clientDto = new ClientDto(client.getClientId(), client.getScopes(), client.getClientName());
                SecurityContextHolder.getContext().setAuthentication(new ClientIdToken(clientDto));
            });
        }

        filterChain.doFilter(request, response);
    }
}
