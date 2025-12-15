package com.localexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    
    private Long id;
    
    @NotBlank(message = "Le contenu du message est obligatoire")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    private String contenu;
    
    private Boolean lu;
    
    private Long expediteurId;
    private String expediteurNom;
    private String expediteurPhoto;
    
    private Long destinataireId;
    private String destinataireNom;
    private String destinatairePhoto;
    
    @NotNull(message = "L'ID de l'échange est obligatoire")
    private Long exchangeRequestId;
    
    private LocalDateTime createdAt;

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

	public Long getExpediteurId() {
		return expediteurId;
	}

	public void setExpediteurId(Long expediteurId) {
		this.expediteurId = expediteurId;
	}

	public String getExpediteurNom() {
		return expediteurNom;
	}

	public void setExpediteurNom(String expediteurNom) {
		this.expediteurNom = expediteurNom;
	}

	public String getExpediteurPhoto() {
		return expediteurPhoto;
	}

	public void setExpediteurPhoto(String expediteurPhoto) {
		this.expediteurPhoto = expediteurPhoto;
	}

	public Long getDestinataireId() {
		return destinataireId;
	}

	public void setDestinataireId(Long destinataireId) {
		this.destinataireId = destinataireId;
	}

	public String getDestinataireNom() {
		return destinataireNom;
	}

	public void setDestinataireNom(String destinataireNom) {
		this.destinataireNom = destinataireNom;
	}

	public String getDestinatairePhoto() {
		return destinatairePhoto;
	}

	public void setDestinatairePhoto(String destinatairePhoto) {
		this.destinatairePhoto = destinatairePhoto;
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