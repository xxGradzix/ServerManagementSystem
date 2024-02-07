package com.xxgradzix.ServerManagementSystem.user.service;

import com.xxgradzix.ServerManagementSystem.user.Role;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void grantAdminAuthority(Long userId) {
        UserEntity user = userEntityRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().add(Role.ADMIN);
        userEntityRepository.save(user);
    }
    public void createHeadAdminIfNotExists() {
        final String adminName = "admin";
        Optional<UserEntity> admin = userEntityRepository.findByEmailIgnoreCase(adminName);

        if (admin.isEmpty()) {

            UserEntity adminUser = UserEntity.builder()
                    .email(adminName)
                    .password(passwordEncoder.encode(adminName))
                    .username(adminName)
                    .roles(Set.of(Role.values()))
                    .build();
            userEntityRepository.save(adminUser);
        }
    }
}
