package com.localexchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(length = 2000, nullable = false)
    private String contenu;
    
    @Column(nullable = false)
    private Boolean lu = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id", nullable = false)
    private User expediteur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User destinataire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_request_id", nullable = false)
    private ExchangeRequest exchangeRequest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public Boolean getLu() {
		return lu;
	}

	public void setLu(Boolean lu) {
		this.lu = lu;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User getExpediteur() {
		return expediteur;
	}

	public void setExpediteur(User expediteur) {
		this.expediteur = expediteur;
	}

	public User getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(User destinataire) {
		this.destinataire = destinataire;
	}

	public ExchangeRequest getExchangeRequest() {
		return exchangeRequest;
	}

	public void setExchangeRequest(ExchangeRequest exchangeRequest) {
		this.exchangeRequest = exchangeRequest;
	}
    
    
}