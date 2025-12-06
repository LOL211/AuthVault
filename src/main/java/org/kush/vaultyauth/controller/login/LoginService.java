package org.kush.vaultyauth.controller.login;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.RegisterRequestDto;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginService
{
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    @Value("${spring.application.name}")
    private String appName;
    private final static long expiryInSeconds = 600;
    private final PasswordEncoder passwordEncoder;

    public String generateToken(ClientIdToken client, RegisterRequestDto registerRequestDto) throws Exception {
        String email = registerRequestDto.email();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(registerRequestDto.password(), user.getPassword()))
        {
            throw new RuntimeException("Invalid password");
        }

        var now = Instant.now();
        if (
                user.getClients().stream().map(Client::getClientId)
                .noneMatch(uuid -> uuid.equals(client.getCredentials()))
        )
        {
            throw new Exception("User is not authorized for this client");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(appName)
                .subject(user.getId().toString())
                .claim("scope", client.getClientDto().scopes())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
