package org.kush.vaultyauth.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "client")
public class Client
{
    @Id
    private UUID clientId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_scopes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "scope")
    private Set<String> scopes;

    @Column
    private String clientSecret;

    @Column(unique = true)
    private String clientName;
}
