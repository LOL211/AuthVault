package org.kush.vaultyauth.database.repository;

import org.kush.vaultyauth.database.model.Client;
import org.kush.vaultyauth.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByEmail(String email);

    @Query("select c from User u join u.clients c where u.id = :userId")
    List<Client> findALlClientsOfAUser(@Param("userId") UUID userId);
}
