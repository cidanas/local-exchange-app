package com.localexchange.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exchange_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(length = 500, nullable = false)
    private String offreEnRetour;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate dateEchange;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus statut = ExchangeStatus.PENDING;
    
    @Column(length = 1000)
    private String messageInitial;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private User beneficiaire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donateur_id", nullable = false)
    private User donateur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_listing_id")
    private ItemListing itemListing;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_listing_id")
    private SkillListing skillListing;
    
    @OneToMany(mappedBy = "exchangeRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
    
    @OneToOne(mappedBy = "exchangeRequest", cascade = CascadeType.ALL)
    private Review review;
}