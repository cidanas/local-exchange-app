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
}