package org.kush.vaultyauth.controller.user;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.controller.dto.UserInfoResponse;
import org.kush.vaultyauth.database.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication)
    {
        var clientId = (UUID) authentication.getPrincipal();
        User u = userService.getUser(clientId);
        if (u == null)
            throw new IllegalArgumentException("Invalid user id!");

        UserInfoResponse response = new UserInfoResponse(u.getUsername(), u.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('userinfo')")
    public ResponseEntity<Map<UUID, String>> createUser(@RequestBody List<UUID> userIds)
    {
        return ResponseEntity.ok(userService.getUsers(userIds));
    }
}
