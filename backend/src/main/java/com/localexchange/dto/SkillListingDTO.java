package com.localexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillListingDTO {
    
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;
    
    @NotBlank(message = "Les disponibilités sont obligatoires")
    private String disponibilites;
    
    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaireEchange;
    
    private String images;
    
    private Boolean actif;
    
    private Long ownerId;
    private String ownerNom;
    private String ownerPhoto;
    private String ownerLocalisation;
    private Double ownerRating;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerNom() {
		return ownerNom;
	}
	public void setOwnerNom(String ownerNom) {
		this.ownerNom = ownerNom;
	}
	public String getOwnerPhoto() {
		return ownerPhoto;
	}
	public void setOwnerPhoto(String ownerPhoto) {
		this.ownerPhoto = ownerPhoto;
	}
	public String getOwnerLocalisation() {
		return ownerLocalisation;
	}
	public void setOwnerLocalisation(String ownerLocalisation) {
		this.ownerLocalisation = ownerLocalisation;
	}
	public Double getOwnerRating() {
		return ownerRating;
	}
	public void setOwnerRating(Double ownerRating) {
		this.ownerRating = ownerRating;
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
	
	
    
    
}