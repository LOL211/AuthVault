package org.kush.vaultyauth.controller.token;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.kush.vaultyauth.controller.dto.TokenRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController
{
    @PostMapping
    public ResponseEntity<String> createToken(@Valid @RequestBody TokenRequestDto token,
                                              Authentication authentication)
    {
        throw new NotImplementedException();
    }
}
