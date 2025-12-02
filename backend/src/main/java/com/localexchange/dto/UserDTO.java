package com.localexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String email;
    private String nom;
    private String localisation;
    private String photo;
    private String bio;
    private String phoneNumber;
    private Boolean phoneVerified;
    private Double averageRating;
    private Integer totalExchanges;
    private LocalDateTime createdAt;
}