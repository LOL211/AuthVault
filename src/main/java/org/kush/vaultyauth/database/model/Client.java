package org.kush.vaultyauth.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "client")
public class Client
{
    @Id
    private String clientId;

    @Column
    private String scopes;

    @Column
    private String clientSecret;

    @Column(unique = true)
    private String clientName;

}
