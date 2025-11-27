package org.kush.vaultyauth.controller;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.controller.dto.UserInfoResponse;
import org.kush.vaultyauth.database.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication)
    {
        UserDto authenticated = (UserDto) authentication.getPrincipal();
        UserInfoResponse response = new UserInfoResponse(authenticated.username(), authenticated.email());

        return ResponseEntity.ok(response);
    }
}
