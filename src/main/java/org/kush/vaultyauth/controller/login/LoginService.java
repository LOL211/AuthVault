package org.kush.vaultyauth.controller.login;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.controller.OAuth2Errors;
import org.kush.vaultyauth.controller.OAuthException;
import org.kush.vaultyauth.controller.dto.OAuthTokenResponse;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    private final ClientRepository clientRepository;

    public String generateToken(User user, String password) {

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new RuntimeException("Invalid password");
        }

        var now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(appName)
                .subject(user.getId().toString())
                .claim("scope", user.getClient().getScopes())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public OAuthTokenResponse generateToken(Map<String, String> inputMap, String authorizationHeader) throws OAuthException {
        String authType = inputMap.getOrDefault("grant_type", null);

        if (authType == null)
        {
           throw new OAuthException(OAuth2Errors.invalid_request);
        }

        var claims = switch (authType) {
            case "client_credentials" -> clientCredentialFlow(authorizationHeader);
            case "password" -> passwordFLow(inputMap);
            default -> throw  new OAuthException(OAuth2Errors.unsupported_grant_type);
        };

        return new OAuthTokenResponse(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), "Bearer", expiryInSeconds);
    }

    private JwtClaimsSet clientCredentialFlow(String authorizationHeader) throws OAuthException {

        if (StringUtils.isBlank(authorizationHeader))
            throw new OAuthException(OAuth2Errors.invalid_request);

        String base64String = authorizationHeader.replace("Basic ", "");
        String decoded = new String(Base64.getDecoder().decode(base64String));

        String clientId = decoded.split(":")[0];
        String clientSecret = decoded.split(":")[1];

        Client client = clientRepository.findById(UUID.fromString(clientId))
                .orElseThrow(() -> new OAuthException(OAuth2Errors.invalid_client));

        if (!passwordEncoder.matches(clientSecret, client.getClientSecret()))
        {
            throw new OAuthException(OAuth2Errors.invalid_client);
        }
        return buildToken(clientId, client.getScopes());
    }

    private JwtClaimsSet passwordFLow(Map<String, String> inputMap) throws OAuthException {
        var userName = inputMap.getOrDefault("username", null);
        var password = inputMap.getOrDefault("password", null);

        if (userName == null || password == null)
            throw new OAuthException(OAuth2Errors.invalid_request);

        User user = userRepository.findByEmail(userName).orElseThrow(() -> new OAuthException(OAuth2Errors.invalid_client));

        var client = clientRepository.findById(user.getClient().getClientId());

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new OAuthException(OAuth2Errors.invalid_client);
        }

        return buildToken(String.valueOf(user.getId()), client.get().getScopes());
    }

    private JwtClaimsSet buildToken(String clientId, Set<String> scopes) throws OAuthException {
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(appName)
                .subject(clientId)
                .claim("scope", scopes)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                .build();
    }
}
