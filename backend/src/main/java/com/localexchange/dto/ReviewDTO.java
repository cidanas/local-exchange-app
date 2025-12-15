package com.localexchange.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    
    private Long id;
    
    @NotNull(message = "La notation est obligatoire")
    @Min(value = 1, message = "La notation doit être entre 1 et 5")
    @Max(value = 5, message = "La notation doit être entre 1 et 5")
    private Integer notation;
    
    @Size(max = 1000, message = "Le commentaire ne peut pas dépasser 1000 caractères")
    private String commentaire;
    
    private Long reviewerId;
    private String reviewerNom;
    private String reviewerPhoto;
    
    private Long revieweeId;
    private String revieweeNom;
    
    @NotNull(message = "L'ID de l'échange est obligatoire")
    private Long exchangeRequestId;
    
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNotation() {
		return notation;
	}

	public void setNotation(Integer notation) {
		this.notation = notation;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerNom() {
		return reviewerNom;
	}

	public void setReviewerNom(String reviewerNom) {
		this.reviewerNom = reviewerNom;
	}

	public String getReviewerPhoto() {
		return reviewerPhoto;
	}

	public void setReviewerPhoto(String reviewerPhoto) {
		this.reviewerPhoto = reviewerPhoto;
	}

	public Long getRevieweeId() {
		return revieweeId;
	}

	public void setRevieweeId(Long revieweeId) {
		this.revieweeId = revieweeId;
	}

	public String getRevieweeNom() {
		return revieweeNom;
	}

	public void setRevieweeNom(String revieweeNom) {
		this.revieweeNom = revieweeNom;
	}

	public Long getExchangeRequestId() {
		return exchangeRequestId;
	}

	public void setExchangeRequestId(Long exchangeRequestId) {
		this.exchangeRequestId = exchangeRequestId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}