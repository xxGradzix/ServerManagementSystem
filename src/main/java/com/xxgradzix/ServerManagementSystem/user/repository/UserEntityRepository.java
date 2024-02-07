package com.xxgradzix.ServerManagementSystem.user.repository;

import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailIgnoreCase(String username);
}
