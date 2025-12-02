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
}