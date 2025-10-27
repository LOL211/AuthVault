package org.kush.vaultyauth.controller;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;

@RestController
@RequestMapping("/api/public")
public class PublicKeyController
{
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @GetMapping
    public ResponseEntity<String> getPublicKey() throws Exception
    {
        byte[] encoded = publicKey.getEncoded();
        String base64Key = Base64.encode(encoded).toString();
        return ResponseEntity.ok(base64Key);
    }
}
