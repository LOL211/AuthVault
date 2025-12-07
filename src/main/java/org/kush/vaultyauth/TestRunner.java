package org.kush.vaultyauth;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("local")
public class TestRunner implements CommandLineRunner
{
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception
    {
        Client client = new Client();
        client.setClientId(UUID.fromString("416ff23e-7d8d-471b-ab1a-bffb54b097b1"));
        client.setClientName("shareAppApi");
        client.setScopes(Set.of("userinfo"));
        client.setClientSecret(passwordEncoder.encode("hYwMEDtRd2qfVbPeBzz"));
        client = clientRepository.save(client);
    }
}
