package org.kush.vaultyauth.controller.token;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.TokenRequestDto;
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
public class TokenService
{
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    @Value("${spring.application.name}")
    private String appName;
    private final static long expiryInSeconds = 600;
    private final PasswordEncoder passwordEncoder;

    public String generateToken(ClientIdToken client, TokenRequestDto tokenRequestDto)
    {
        String email = tokenRequestDto.email();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(tokenRequestDto.password(), user.getPassword()))
        {
            throw new RuntimeException("Invalid password");
        }

        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(appName)
                .subject(user.getEmail())
                .claim("scope", client.getClientDto().scopes())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
