package com.localexchange.model;

import jakarta.persistence.*;
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
@Table(name = "item_listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String titre;
    
    @NotBlank
    @Column(length = 1000, nullable = false)
    private String description;
    
    @NotBlank
    @Column(nullable = false)
    private String categorie;
    
    @Column(length = 2000)
    private String images;
    
    @Column(nullable = false)
    private Boolean disponibilite = true;
    
    @Column(length = 500)
    private String commentaireEchange;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "itemListing", cascade = CascadeType.ALL)
    private List<ExchangeRequest> exchangeRequests = new ArrayList<>();
}