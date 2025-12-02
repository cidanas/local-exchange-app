package com.localexchange.controller;

import com.localexchange.dto.*;
import com.localexchange.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        UserDTO user = authService.register(registerDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    /**
     * Connexion d'un utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer le profil de l'utilisateur connecté
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    
    /**
     * Mettre à jour le profil de l'utilisateur
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody ProfileUpdateDTO profileUpdateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UserDTO updatedUser = authService.updateProfile(userDetails.getUsername(), profileUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}