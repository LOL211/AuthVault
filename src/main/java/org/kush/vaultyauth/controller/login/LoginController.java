package org.kush.vaultyauth.controller.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController
{
    private final LoginService loginService;
    @PostMapping
    public ResponseEntity<String> createToken(@Valid @RequestBody RegisterRequestDto token,
                                              Authentication authentication) throws Exception {
        return ResponseEntity.ok(loginService.generateToken((ClientIdToken) authentication, token));
    }
}
