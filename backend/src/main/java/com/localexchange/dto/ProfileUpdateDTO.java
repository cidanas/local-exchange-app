package com.localexchange.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    
    private String nom;
    
    private String localisation;
    
    @Size(max = 500, message = "La biographie ne peut pas dépasser 500 caractères")
    private String bio;
    
    private String photo;
    
    private String phoneNumber;
}