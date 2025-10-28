package org.kush.vaultyauth;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class TestRunner implements CommandLineRunner
{
    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception
    {
        Client client = new Client();
        client.setClientId("test");
        client.setClientName("testClient");
        client.setScopes("scope1;scope2");
        clientRepository.save(client);
    }
}
