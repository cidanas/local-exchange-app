package com.localexchange.service;

import com.localexchange.dto.ReviewDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.*;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ExchangeRequestRepository mockExchangeRequestRepository;

    @Mock
    private NotificationService mockNotificationService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReviewSuccess() {
        // Arrange
        User reviewer = new User();
        reviewer.setId(1L);
        reviewer.setEmail("reviewer@mail.fr");
        reviewer.setNom("Alice");

        User reviewee = new User();
        reviewee.setId(2L);
        reviewee.setEmail("reviewee@mail.fr");
        reviewee.setNom("Bob");

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);
        exchangeRequest.setDonateur(reviewer);
        exchangeRequest.setBeneficiaire(reviewee);
        exchangeRequest.setStatut(ExchangeStatus.COMPLETED);

        ReviewDTO dto = new ReviewDTO();
        dto.setExchangeRequestId(100L);
        dto.setNotation(5);
        dto.setCommentaire("Excellent échange !");

        when(mockUserRepository.findByEmail("reviewer@mail.fr")).thenReturn(Optional.of(reviewer));
        when(mockExchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));
        when(mockReviewRepository.existsByExchangeRequestAndReviewer(exchangeRequest, reviewer)).thenReturn(false);
        when(mockReviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review review = invocation.getArgument(0);
            review.setId(999L);
            return review;
        });
        when(mockNotificationService.createNotification(
                anyLong(), any(), anyString(), anyLong(), any(), any()
        )).thenReturn(new Notification());

        // Act
        ReviewDTO result = reviewService.createReview(dto, "reviewer@mail.fr");

        // Assert
        assertNotNull(result);
        assertEquals(999L, result.getId());
        assertEquals(5, result.getNotation());
        assertEquals("Excellent échange !", result.getCommentaire());
        assertEquals(reviewer.getId(), result.getReviewerId());
        assertEquals(reviewee.getId(), result.getRevieweeId());

        verify(mockNotificationService, times(1)).createNotification(
                eq(reviewee.getId()),
                eq(NotificationType.REVIEW_RECEIVED),
                contains("Alice"),
                eq(exchangeRequest.getId()),
                isNull(),
                isNull()
        );
    }

    @Test
    public void testCreateReviewThrowsWhenExchangeNotCompleted() {
        User reviewer = new User();
        reviewer.setId(1L);
        reviewer.setEmail("reviewer@mail.fr");

        User reviewee = new User();
        reviewee.setId(2L);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);
        exchangeRequest.setDonateur(reviewer);
        exchangeRequest.setBeneficiaire(reviewee);
        exchangeRequest.setStatut(ExchangeStatus.PENDING); // pas terminé

        ReviewDTO dto = new ReviewDTO();
        dto.setExchangeRequestId(100L);

        when(mockUserRepository.findByEmail("reviewer@mail.fr")).thenReturn(Optional.of(reviewer));
        when(mockExchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));

        assertThrows(IllegalStateException.class,
                () -> reviewService.createReview(dto, "reviewer@mail.fr"));
    }

    @Test
    public void testCreateReviewThrowsWhenAlreadyExists() {
        User reviewer = new User();
        reviewer.setId(1L);
        reviewer.setEmail("reviewer@mail.fr");

        User reviewee = new User();
        reviewee.setId(2L);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);
        exchangeRequest.setDonateur(reviewer);
        exchangeRequest.setBeneficiaire(reviewee);
        exchangeRequest.setStatut(ExchangeStatus.COMPLETED);

        ReviewDTO dto = new ReviewDTO();
        dto.setExchangeRequestId(100L);

        when(mockUserRepository.findByEmail("reviewer@mail.fr")).thenReturn(Optional.of(reviewer));
        when(mockExchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));
        when(mockReviewRepository.existsByExchangeRequestAndReviewer(exchangeRequest, reviewer)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> reviewService.createReview(dto, "reviewer@mail.fr"));
    }
}
