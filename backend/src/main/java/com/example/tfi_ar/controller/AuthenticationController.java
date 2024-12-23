package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.AuthenticationRequest;
import com.example.tfi_ar.dto.AuthenticationResponse;
import com.example.tfi_ar.dto.RegisterRequest;
import com.example.tfi_ar.dto.RoleResponse;
import com.example.tfi_ar.exception.EmailAlreadyInUseException;
import com.example.tfi_ar.exception.InvalidRoleException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws EmailAlreadyInUseException, InvalidRoleException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, UserNotFoundException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/role")
    public ResponseEntity<RoleResponse> getCurrentRole() {
        return ResponseEntity.ok(authenticationService.getCurrentRole());
    }
}
