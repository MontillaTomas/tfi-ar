package com.example.tfi_ar.service;

import com.example.tfi_ar.config.JwtService;
import com.example.tfi_ar.dto.AuthenticationRequest;
import com.example.tfi_ar.dto.AuthenticationResponse;
import com.example.tfi_ar.dto.RegisterRequest;
import com.example.tfi_ar.dto.RoleResponse;
import com.example.tfi_ar.exception.EmailAlreadyInUseException;
import com.example.tfi_ar.exception.InvalidRoleException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.Role;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @NonNull HttpServletRequest request;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws EmailAlreadyInUseException, InvalidRoleException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if (!Arrays.asList(Role.values()).contains(request.getRole())) {
            throw new InvalidRoleException("Invalid role");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        var accessToken = jwtService.generateAccessToken(user, user.getId());
        var refreshToken = jwtService.generateRefreshToken(user, user.getId());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var jwtToken = jwtService.generateAccessToken(user, user.getId());
        var refreshToken = jwtService.generateRefreshToken(user, user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UserNotFoundException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            if(jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateAccessToken(user, user.getId());
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    public Integer getUserIdFromToken() {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        return jwtService.extractUserId(jwt);
    }

    public RoleResponse getCurrentRole() {
        var user = userRepository.findById(getUserIdFromToken())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return RoleResponse.builder()
                .role(user.getRole().name())
                .build();
    }
}
