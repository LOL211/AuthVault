package org.kush.vaultyauth.controller.register;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.config.ClientIdToken;
import org.kush.vaultyauth.controller.dto.UserRegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/register")
public class RegisterController
{
    private final RegisterService registerService;

    @PostMapping
    private ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest, Authentication principal)
    {
        return ResponseEntity.ok(registerService.register(userRegisterRequest, (ClientIdToken) principal));
    }
}
