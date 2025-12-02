package com.localexchange.repository;

import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.Review;
import com.localexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByRevieweeOrderByCreatedAtDesc(User reviewee);
    
    Optional<Review> findByExchangeRequestAndReviewer(ExchangeRequest exchangeRequest, User reviewer);
    
    Boolean existsByExchangeRequestAndReviewer(ExchangeRequest exchangeRequest, User reviewer);
    
    @Query("SELECT AVG(r.notation) FROM Review r WHERE r.reviewee.id = :userId")
    Double getAverageRatingByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewee.id = :userId")
    Long countReviewsByUserId(@Param("userId") Long userId);
}