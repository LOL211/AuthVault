package org.kush.vaultyauth.config;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter
{
    private final UserRepository userRepository;
    private final RSAPublicKey publicKey;
    private final ClientRepository clientRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is empty");
            return;
        }
        var jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

        var decodedToken = jwtDecoder.decode(token.split("Bearer ")[1]);

        if (decodedToken == null || decodedToken.getExpiresAt() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Failed to decode token");
            return;
        }

        if (Instant.now().isAfter(decodedToken.getExpiresAt()))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            return;
        }

        User user = userRepository.findByIdWithClient(UUID.fromString(decodedToken.getSubject())).orElse(null);

        if (user != null) {
            SecurityContextHolder.getContext().setAuthentication(new UserToken(user.getId(), user.getClient().getScopes()));
            filterChain.doFilter(request, response);
            return;
        }

        Client client = clientRepository.findById(UUID.fromString(decodedToken.getSubject())).orElse(null);

        if (client != null)
        {
            SecurityContextHolder.getContext().setAuthentication(new UserToken(client.getClientId(), client.getScopes()));
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Id not found");

    }

}
