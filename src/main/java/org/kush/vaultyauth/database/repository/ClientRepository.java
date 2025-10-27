package org.kush.vaultyauth.database.repository;

import org.kush.vaultyauth.database.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>
{
    Optional<Client> findClientByClientId(String clientId);
}
