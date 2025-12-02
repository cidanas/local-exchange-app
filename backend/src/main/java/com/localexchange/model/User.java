package com.localexchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String password;
    
    @NotBlank
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String localisation;
    
    @Column(length = 500)
    private String photo;
    
    @Column(length = 500)
    private String bio;
    
    private String phoneNumber;
    
    @Column(nullable = false)
    private Boolean phoneVerified = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemListing> itemListings = new ArrayList<>();
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillListing> skillListings = new ArrayList<>();
    
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    private List<Review> reviewsGiven = new ArrayList<>();
    
    @OneToMany(mappedBy = "reviewee", cascade = CascadeType.ALL)
    private List<Review> reviewsReceived = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}