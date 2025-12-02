package com.localexchange.controller;

import com.localexchange.dto.ReviewDTO;
import com.localexchange.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * Créer un nouvel avis
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @Valid @RequestBody ReviewDTO reviewDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO, userDetails.getUsername());
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Handle constraint violations and already reviewed
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            error.put("error", "DUPLICATE_REVIEW");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }
    
    /**
     * Récupérer les avis d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReviews(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }
    
    /**
     * Récupérer la note moyenne d'un utilisateur
     */
    @GetMapping("/user/{userId}/average")
    public ResponseEntity<?> getAverageRating(@PathVariable Long userId) {
        Double average = reviewService.getAverageRating(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("average", average);
        response.put("userId", userId);
        
        return ResponseEntity.ok(response);
    }
}