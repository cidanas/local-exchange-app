package com.localexchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exchange_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(length = 500, nullable = false)
    private String offreEnRetour;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate dateEchange;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus statut = ExchangeStatus.PENDING;
    
    @Column(length = 1000)
    private String messageInitial;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private User beneficiaire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donateur_id", nullable = false)
    private User donateur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_listing_id")
    private ItemListing itemListing;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_listing_id")
    private SkillListing skillListing;
    
    @OneToMany(mappedBy = "exchangeRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
    
    @OneToOne(mappedBy = "exchangeRequest", cascade = CascadeType.ALL)
    private Review review;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOffreEnRetour() {
		return offreEnRetour;
	}

	public void setOffreEnRetour(String offreEnRetour) {
		this.offreEnRetour = offreEnRetour;
	}

	public LocalDate getDateEchange() {
		return dateEchange;
	}

	public void setDateEchange(LocalDate dateEchange) {
		this.dateEchange = dateEchange;
	}

	public ExchangeStatus getStatut() {
		return statut;
	}

	public void setStatut(ExchangeStatus statut) {
		this.statut = statut;
	}

	public String getMessageInitial() {
		return messageInitial;
	}

	public void setMessageInitial(String messageInitial) {
		this.messageInitial = messageInitial;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getBeneficiaire() {
		return beneficiaire;
	}

	public void setBeneficiaire(User beneficiaire) {
		this.beneficiaire = beneficiaire;
	}

	public User getDonateur() {
		return donateur;
	}

	public void setDonateur(User donateur) {
		this.donateur = donateur;
	}

	public ItemListing getItemListing() {
		return itemListing;
	}

	public void setItemListing(ItemListing itemListing) {
		this.itemListing = itemListing;
	}

	public SkillListing getSkillListing() {
		return skillListing;
	}

	public void setSkillListing(SkillListing skillListing) {
		this.skillListing = skillListing;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
    
    
}