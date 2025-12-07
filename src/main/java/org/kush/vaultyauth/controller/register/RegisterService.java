package org.kush.vaultyauth.controller.register;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.UserRegisterRequest;
import org.kush.vaultyauth.controller.token.LoginService;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService
{
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    public String register(UserRegisterRequest userRegisterRequest, ClientIdToken clientToken) {
        Client client = clientRepository.findById(UUID.fromString(clientToken.getName()))
                .orElseThrow(()->new IllegalArgumentException("Client not found"));

        userRepository.findByEmail(userRegisterRequest.email())
                .ifPresent(user -> {throw new IllegalArgumentException("User "+user.getEmail()+" already exists");});

        User u = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(passwordEncoder.encode(userRegisterRequest.password()))
                .client(client)
                .build();

        u = userRepository.save(u);
        return loginService.generateToken(u,  userRegisterRequest.password());
    }
}
