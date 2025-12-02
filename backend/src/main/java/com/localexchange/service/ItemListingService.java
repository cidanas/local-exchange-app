package com.localexchange.service;

import com.localexchange.dto.ItemDetailDTO;
import com.localexchange.dto.ItemListingDTO;
import com.localexchange.dto.UserDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.ItemListing;
import com.localexchange.model.User;
import com.localexchange.repository.ItemListingRepository;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemListingService {
    
    @Autowired
    private ItemListingRepository itemListingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Créer une nouvelle annonce d'objet
     */
    public ItemListingDTO createItem(ItemListingDTO dto, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", ownerEmail));
        
        ItemListing item = new ItemListing();
        item.setTitre(dto.getTitre());
        item.setDescription(dto.getDescription());
        item.setCategorie(dto.getCategorie());
        item.setImages(dto.getImages());
        item.setDisponibilite(true);
        item.setCommentaireEchange(dto.getCommentaireEchange());
        item.setOwner(owner);
        
        ItemListing savedItem = itemListingRepository.save(item);
        
        return convertToDTO(savedItem);
    }
    
    /**
     * Récupérer tous les objets avec filtres
     */
    public Page<ItemListingDTO> getAllItems(String categorie, String search, Pageable pageable) {
        Page<ItemListing> items;
        
        if (categorie != null && !categorie.isEmpty() && search != null && !search.isEmpty()) {
            items = itemListingRepository.findByCategorieAndTitreContainingIgnoreCase(categorie, search, pageable);
        } else if (categorie != null && !categorie.isEmpty()) {
            items = itemListingRepository.findByCategorie(categorie, pageable);
        } else if (search != null && !search.isEmpty()) {
            items = itemListingRepository.findByTitreContainingIgnoreCase(search, pageable);
        } else {
            items = itemListingRepository.findAll(pageable);
        }
        
        return items.map(this::convertToDTO);
    }
    
    /**
     * Récupérer un objet par ID
     */
    public ItemDetailDTO getItemById(Long id) {
        ItemListing item = itemListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objet", "id", id));
        
        return convertToDetailDTO(item);
    }
    
    /**
     * Mettre à jour un objet
     */
    public ItemListingDTO updateItem(Long id, ItemListingDTO dto, String userEmail) {
        ItemListing item = itemListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objet", "id", id));
        
        // Vérifier que l'utilisateur est le propriétaire
        if (!item.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier cet objet");
        }
        
        item.setTitre(dto.getTitre());
        item.setDescription(dto.getDescription());
        item.setCategorie(dto.getCategorie());
        item.setImages(dto.getImages());
        item.setDisponibilite(dto.getDisponibilite());
        item.setCommentaireEchange(dto.getCommentaireEchange());
        
        ItemListing updatedItem = itemListingRepository.save(item);
        
        return convertToDTO(updatedItem);
    }
    
    /**
     * Supprimer un objet
     */
    public void deleteItem(Long id, String userEmail) {
        ItemListing item = itemListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objet", "id", id));
        
        // Vérifier que l'utilisateur est le propriétaire
        if (!item.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à supprimer cet objet");
        }
        
        itemListingRepository.delete(item);
    }
    
    /**
     * Récupérer les objets d'un utilisateur
     */
    public List<ItemListingDTO> getItemsByOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", ownerEmail));
        
        List<ItemListing> items = itemListingRepository.findByOwnerOrderByCreatedAtDesc(owner);
        
        return items.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertir ItemListing en ItemListingDTO
     */
    private ItemListingDTO convertToDTO(ItemListing item) {
        ItemListingDTO dto = new ItemListingDTO();
        dto.setId(item.getId());
        dto.setTitre(item.getTitre());
        dto.setDescription(item.getDescription());
        dto.setCategorie(item.getCategorie());
        dto.setImages(item.getImages());
        dto.setDisponibilite(item.getDisponibilite());
        dto.setCommentaireEchange(item.getCommentaireEchange());
        dto.setOwnerId(item.getOwner().getId());
        dto.setOwnerNom(item.getOwner().getNom());
        dto.setOwnerPhoto(item.getOwner().getPhoto());
        dto.setOwnerLocalisation(item.getOwner().getLocalisation());
        dto.setOwnerRating(reviewRepository.getAverageRatingByUserId(item.getOwner().getId()));
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        
        return dto;
    }
    
    /**
     * Convertir ItemListing en ItemDetailDTO
     */
    private ItemDetailDTO convertToDetailDTO(ItemListing item) {
        ItemDetailDTO dto = new ItemDetailDTO();
        dto.setId(item.getId());
        dto.setTitre(item.getTitre());
        dto.setDescription(item.getDescription());
        dto.setCategorie(item.getCategorie());
        dto.setImages(item.getImages());
        dto.setDisponibilite(item.getDisponibilite());
        dto.setCommentaireEchange(item.getCommentaireEchange());
        dto.setOwnerId(item.getOwner().getId());
        dto.setOwnerNom(item.getOwner().getNom());
        dto.setOwnerPhoto(item.getOwner().getPhoto());
        dto.setOwnerLocalisation(item.getOwner().getLocalisation());
        dto.setOwnerRating(reviewRepository.getAverageRatingByUserId(item.getOwner().getId()));
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        
        // Owner complet
        UserDTO ownerDTO = new UserDTO();
        ownerDTO.setId(item.getOwner().getId());
        ownerDTO.setEmail(item.getOwner().getEmail());
        ownerDTO.setNom(item.getOwner().getNom());
        ownerDTO.setLocalisation(item.getOwner().getLocalisation());
        ownerDTO.setPhoto(item.getOwner().getPhoto());
        ownerDTO.setBio(item.getOwner().getBio());
        ownerDTO.setAverageRating(reviewRepository.getAverageRatingByUserId(item.getOwner().getId()));
        
        dto.setOwner(ownerDTO);
        
        return dto;
    }
}