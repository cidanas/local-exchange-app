package com.localexchange.service;

import com.localexchange.dto.ReviewDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.*;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Créer un avis
     */
    @Transactional
    public ReviewDTO createReview(ReviewDTO dto, String reviewerEmail) {
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", reviewerEmail));
        
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(dto.getExchangeRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Échange", "id", dto.getExchangeRequestId()));
        
        // Vérifier que l'échange est terminé
        if (exchangeRequest.getStatut() != ExchangeStatus.COMPLETED) {
            throw new IllegalStateException("Vous ne pouvez laisser un avis que pour un échange terminé");
        }
        
        // Vérifier que l'utilisateur fait partie de l'échange
        if (!exchangeRequest.getDonateur().getId().equals(reviewer.getId()) &&
            !exchangeRequest.getBeneficiaire().getId().equals(reviewer.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à laisser un avis pour cet échange");
        }
        
        // Vérifier qu'un avis n'existe pas déjà (première vérification)
        if (reviewRepository.existsByExchangeRequestAndReviewer(exchangeRequest, reviewer)) {
            throw new IllegalStateException("Vous avez déjà laissé un avis pour cet échange");
        }
        
        // Déterminer le reviewee
        User reviewee = exchangeRequest.getDonateur().getId().equals(reviewer.getId())
                ? exchangeRequest.getBeneficiaire()
                : exchangeRequest.getDonateur();
        
        // Vérifier à nouveau juste avant de sauvegarder (protection contre les race conditions)
        if (reviewRepository.existsByExchangeRequestAndReviewer(exchangeRequest, reviewer)) {
            throw new IllegalStateException("Un avis a déjà été créé pour cet échange. Veuillez rafraîchir la page.");
        }
        
        Review review = new Review();
        review.setNotation(dto.getNotation());
        review.setCommentaire(dto.getCommentaire());
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setExchangeRequest(exchangeRequest);
        
        try {
            Review savedReview = reviewRepository.save(review);

            // Créer notification pour le reviewee
            String message = String.format("%s a laissé un avis sur votre profil", reviewer.getNom());
            notificationService.createNotification(
                reviewee.getId(),
                NotificationType.REVIEW_RECEIVED,
                message,
                exchangeRequest.getId(),
                null,
                null
            );

            return convertToDTO(savedReview);
        } catch (DataIntegrityViolationException ex) {
            // Cela peut arriver si la contrainte unique en base n'est pas correcte
            throw new IllegalStateException("Impossible de créer l'avis : un avis existe déjà ou une contrainte en base empêche l'opération.", ex);
        }
    }
    
    /**
     * Récupérer les avis d'un utilisateur
     */
    public List<ReviewDTO> getUserReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userId));
        
        List<Review> reviews = reviewRepository.findByRevieweeOrderByCreatedAtDesc(user);
        
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculer la note moyenne d'un utilisateur
     */
    public Double getAverageRating(Long userId) {
        Double average = reviewRepository.getAverageRatingByUserId(userId);
        return average != null ? average : 0.0;
    }
    
    /**
     * Convertir Review en ReviewDTO
     */
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setNotation(review.getNotation());
        dto.setCommentaire(review.getCommentaire());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerNom(review.getReviewer().getNom());
        dto.setReviewerPhoto(review.getReviewer().getPhoto());
        dto.setRevieweeId(review.getReviewee().getId());
        dto.setRevieweeNom(review.getReviewee().getNom());
        dto.setExchangeRequestId(review.getExchangeRequest().getId());
        dto.setCreatedAt(review.getCreatedAt());
        
        return dto;
    }
}