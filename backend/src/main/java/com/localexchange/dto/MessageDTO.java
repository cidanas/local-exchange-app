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
}