package com.localexchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill_listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillListing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String titre;
    
    @NotBlank
    @Column(length = 1000, nullable = false)
    private String description;
    
    @NotBlank
    @Column(nullable = false)
    private String disponibilites;
    
    @Column(length = 500)
    private String commentaireEchange;
    
    @Column(length = 2000)
    private String images;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "skillListing", cascade = CascadeType.ALL)
    private List<ExchangeRequest> exchangeRequests = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisponibilites() {
		return disponibilites;
	}

	public void setDisponibilites(String disponibilites) {
		this.disponibilites = disponibilites;
	}

	public String getCommentaireEchange() {
		return commentaireEchange;
	}

	public void setCommentaireEchange(String commentaireEchange) {
		this.commentaireEchange = commentaireEchange;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<ExchangeRequest> getExchangeRequests() {
		return exchangeRequests;
	}

	public void setExchangeRequests(List<ExchangeRequest> exchangeRequests) {
		this.exchangeRequests = exchangeRequests;
	}
    
    
}