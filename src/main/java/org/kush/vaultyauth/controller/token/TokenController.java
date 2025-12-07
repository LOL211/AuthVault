package org.kush.vaultyauth.controller.token;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.controller.OAuth2Errors;
import org.kush.vaultyauth.controller.OAuthException;
import org.kush.vaultyauth.controller.dto.OAuthTokenResponse;
import org.kush.vaultyauth.controller.login.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController
{
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<OAuthTokenResponse> createToken(@RequestBody String info, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) throws Exception {

        if (StringUtils.isBlank(info))
        {
            throw new OAuthException(OAuth2Errors.invalid_request);
        }

        String[] inputs = info.split("&");
        Map<String, String> inputMap = new HashMap<>();

        Arrays.stream(inputs).forEach(input -> {
            String[] keyValue = input.split("=");
          inputMap.put(keyValue[0], keyValue[1]);
        });

        return ResponseEntity.ok(loginService.generateToken(inputMap, authorizationHeader));
    }
}