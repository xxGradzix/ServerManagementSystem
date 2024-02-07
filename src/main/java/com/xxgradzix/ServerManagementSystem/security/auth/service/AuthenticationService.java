package com.xxgradzix.ServerManagementSystem.security.auth.service;

import com.xxgradzix.ServerManagementSystem.security.auth.entity.AuthenticationRequest;
import com.xxgradzix.ServerManagementSystem.security.auth.entity.AuthenticationResponse;
import com.xxgradzix.ServerManagementSystem.security.auth.entity.RegisterRequest;
import com.xxgradzix.ServerManagementSystem.security.auth.exception.UserAlreadyExistAuthenticationException;
import com.xxgradzix.ServerManagementSystem.security.service.JwtService;
import com.xxgradzix.ServerManagementSystem.user.Role;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(@RequestBody RegisterRequest request) {

        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmailIgnoreCase(request.getEmail());

        if(userEntityOptional.isPresent()) {
            throw new UserAlreadyExistAuthenticationException("User with email " + request.getEmail() + " already exists");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(Set.of(Role.USER))
                .build();

        userEntityRepository.save(userEntity);

        String jwtToken = jwtService.generateToken(userEntity);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserDetails userDetails = userEntityRepository.findByEmailIgnoreCase(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

}
