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
}