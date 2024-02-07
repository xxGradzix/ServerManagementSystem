package com.xxgradzix.ServerManagementSystem.user.service;

import com.xxgradzix.ServerManagementSystem.user.Role;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAdminIfNotExists() {
        final String adminName = "admin";
        Optional<UserEntity> admin = userEntityRepository.findByEmailIgnoreCase(adminName);

        if (admin.isEmpty()) {

            UserEntity adminUser = UserEntity.builder()
                    .email(adminName)
                    .password(passwordEncoder.encode(adminName))
                    .username(adminName)
                    .role(Role.ADMIN)
                    .build();
            userEntityRepository.save(adminUser);
        }
    }
}
