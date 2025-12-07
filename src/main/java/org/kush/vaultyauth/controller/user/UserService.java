package org.kush.vaultyauth.controller.user;

import lombok.RequiredArgsConstructor;
import org.kush.vaultyauth.database.model.User;
import org.kush.vaultyauth.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<UUID, String> getUsers(List<UUID> userIds)
    {
        Map<UUID, String> resultMap = new HashMap<>();
        userRepository.findAllById(userIds).forEach(user -> {
            resultMap.put(user.getId(), user.getUsername());
        });

        return resultMap;
    }

    public User getUser(UUID userId)
    {
        return userRepository.findById(userId).orElse(null);
    }
}
