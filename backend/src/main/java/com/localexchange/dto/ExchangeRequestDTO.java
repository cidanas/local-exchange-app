package com.localexchange.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDTO {
    
    private Long id;
    
    @NotBlank(message = "L'offre en retour est obligatoire")
    @Size(max = 500, message = "L'offre ne peut pas dépasser 500 caractères")
    private String offreEnRetour;
    
    @NotNull(message = "La date d'échange est obligatoire")
    @Future(message = "La date d'échange doit être dans le futur")
    private LocalDate dateEchange;
    
    private String statut;
    
    @Size(max = 1000, message = "Le message ne peut pas dépasser 1000 caractères")
    private String messageInitial;
    
    private Long beneficiaireId;
    private String beneficiaireNom;
    private String beneficiairePhoto;
    
    private Long donateurId;
    private String donateurNom;
    private String donateurPhoto;
    
    private Long itemListingId;
    private String itemTitre;
    
    private Long skillListingId;
    private String skillTitre;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getMessageInitial() {
		return messageInitial;
	}
	public void setMessageInitial(String messageInitial) {
		this.messageInitial = messageInitial;
	}
	public Long getBeneficiaireId() {
		return beneficiaireId;
	}
	public void setBeneficiaireId(Long beneficiaireId) {
		this.beneficiaireId = beneficiaireId;
	}
	public String getBeneficiaireNom() {
		return beneficiaireNom;
	}
	public void setBeneficiaireNom(String beneficiaireNom) {
		this.beneficiaireNom = beneficiaireNom;
	}
	public String getBeneficiairePhoto() {
		return beneficiairePhoto;
	}
	public void setBeneficiairePhoto(String beneficiairePhoto) {
		this.beneficiairePhoto = beneficiairePhoto;
	}
	public Long getDonateurId() {
		return donateurId;
	}
	public void setDonateurId(Long donateurId) {
		this.donateurId = donateurId;
	}
	public String getDonateurNom() {
		return donateurNom;
	}
	public void setDonateurNom(String donateurNom) {
		this.donateurNom = donateurNom;
	}
	public String getDonateurPhoto() {
		return donateurPhoto;
	}
	public void setDonateurPhoto(String donateurPhoto) {
		this.donateurPhoto = donateurPhoto;
	}
	public Long getItemListingId() {
		return itemListingId;
	}
	public void setItemListingId(Long itemListingId) {
		this.itemListingId = itemListingId;
	}
	public String getItemTitre() {
		return itemTitre;
	}
	public void setItemTitre(String itemTitre) {
		this.itemTitre = itemTitre;
	}
	public Long getSkillListingId() {
		return skillListingId;
	}
	public void setSkillListingId(Long skillListingId) {
		this.skillListingId = skillListingId;
	}
	public String getSkillTitre() {
		return skillTitre;
	}
	public void setSkillTitre(String skillTitre) {
		this.skillTitre = skillTitre;
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