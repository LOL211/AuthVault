package org.kush.vaultyauth.controller.register;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.UserRegisterRequest;
import org.kush.vaultyauth.controller.token.TokenService;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService
{
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final JwtEncoder jwtEncoder;
    private final TokenService tokenService;

    public String register(UserRegisterRequest userRegisterRequest, ClientIdToken principal)
    {
        Client client = clientRepository.findClientByClientId(principal.getName())
                .orElseThrow(()->new IllegalArgumentException("Client not found"));

        User u = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(userRegisterRequest.password())
                .client(client)
                .build();

        userRepository.save(u);
//        return tokenService()
        return "";
    }
}
