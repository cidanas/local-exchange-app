package com.localexchange.service;

import com.localexchange.dto.*;
import com.localexchange.exception.DuplicateEmailException;
import com.localexchange.exception.InvalidCredentialsException;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.model.User;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.UserRepository;
import com.localexchange.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    public UserDTO register(RegisterDTO registerDTO) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new DuplicateEmailException(registerDTO.getEmail());
        }
        
        // Créer le nouvel utilisateur
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNom(registerDTO.getNom());
        user.setLocalisation(registerDTO.getLocalisation());
        user.setPhoneVerified(false);
        
        User savedUser = userRepository.save(user);
        
        return convertToUserDTO(savedUser);
    }
    
    /**
     * Connexion d'un utilisateur
     */
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = tokenProvider.generateToken(userDetails);
            
            User user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", loginDTO.getEmail()));
            
            UserDTO userDTO = convertToUserDTO(user);
            
            return new LoginResponseDTO(token, userDTO);
            
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
    
    /**
     * Récupérer l'utilisateur connecté
     */
    public UserDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", email));
        
        return convertToUserDTO(user);
    }
    
    /**
     * Mettre à jour le profil utilisateur
     */
    public UserDTO updateProfile(String email, ProfileUpdateDTO profileUpdateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", email));
        
        if (profileUpdateDTO.getNom() != null) {
            user.setNom(profileUpdateDTO.getNom());
        }
        if (profileUpdateDTO.getLocalisation() != null) {
            user.setLocalisation(profileUpdateDTO.getLocalisation());
        }
        if (profileUpdateDTO.getBio() != null) {
            user.setBio(profileUpdateDTO.getBio());
        }
        if (profileUpdateDTO.getPhoto() != null) {
            user.setPhoto(profileUpdateDTO.getPhoto());
        }
        if (profileUpdateDTO.getPhoneNumber() != null) {
            if (!profileUpdateDTO.getPhoneNumber().equals(user.getPhoneNumber())) {
                user.setPhoneNumber(profileUpdateDTO.getPhoneNumber());
                user.setPhoneVerified(false);
            }
        }
        
        User updatedUser = userRepository.save(user);
        
        return convertToUserDTO(updatedUser);
    }
    
    /**
     * Calculer la note moyenne d'un utilisateur
     */
    public Double getAverageRating(Long userId) {
        Double average = reviewRepository.getAverageRatingByUserId(userId);
        return average != null ? average : 0.0;
    }
    
    /**
     * Convertir User en UserDTO
     */
    private UserDTO convertToUserDTO(@NonNull User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNom(user.getNom());
        dto.setLocalisation(user.getLocalisation());
        dto.setPhoto(user.getPhoto());
        dto.setBio(user.getBio());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPhoneVerified(user.getPhoneVerified());
        dto.setAverageRating(getAverageRating(user.getId()));
        dto.setTotalExchanges(0); // TODO: calculer le nombre réel d'échanges
        dto.setCreatedAt(user.getCreatedAt());
        
        return dto;
    }
}