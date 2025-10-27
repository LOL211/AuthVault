package org.kush.vaultyauth.controller;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.controller.dto.UserRegisterRequest;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegisterService
{
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final JwtEncoder jwtEncoder;
    private final static long expiryInSeconds = 600;

    @Value("${spring.application.name}")
    private String appName;

    public String register(UserRegisterRequest userRegisterRequest)
    {
        var client = clientRepository.findClientByClientId(userRegisterRequest.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect clientId"));

        User u = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(userRegisterRequest.password())
                .client(client)
                .build();

        userRepository.save(u);

        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(appName)
                .subject(u.getEmail())
                .claim("scope", client.getScopes())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
