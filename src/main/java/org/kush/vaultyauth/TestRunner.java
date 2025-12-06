package org.kush.vaultyauth;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
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

    @Override
    public void run(String... args) throws Exception
    {
        Client client = new Client();
        client.setClientId(UUID.fromString("a1c2049a-8850-4cbc-aae3-b4a3b0d7e078"));
        client.setClientName("testClient");
        client.setScopes("api.share.app;userinfo");
        client = clientRepository.save(client);

        User u = userRepository.findById(UUID.fromString("013c8519-f04e-46d0-9c8c-9be9a8dae3a8")).get();
        u.setClients(Set.of(client));
        userRepository.save(u);
    }
}
