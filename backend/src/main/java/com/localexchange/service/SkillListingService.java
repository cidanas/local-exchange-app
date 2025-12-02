package com.localexchange.service;

import com.localexchange.dto.SkillDetailDTO;
import com.localexchange.dto.SkillListingDTO;
import com.localexchange.dto.UserDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.SkillListing;
import com.localexchange.model.User;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.SkillListingRepository;
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
public class SkillListingService {
    
    @Autowired
    private SkillListingRepository skillListingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Créer une nouvelle annonce de compétence
     */
    public SkillListingDTO createSkill(SkillListingDTO dto, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", ownerEmail));
        
        SkillListing skill = new SkillListing();
        skill.setTitre(dto.getTitre());
        skill.setDescription(dto.getDescription());
        skill.setDisponibilites(dto.getDisponibilites());
        skill.setCommentaireEchange(dto.getCommentaireEchange());
        skill.setActif(true);
        skill.setOwner(owner);
                if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                    skill.setImages(dto.getImages());
                }
        
        SkillListing savedSkill = skillListingRepository.save(skill);
        
        return convertToDTO(savedSkill);
    }
    
    /**
     * Récupérer toutes les compétences avec recherche
     */
    public Page<SkillListingDTO> getAllSkills(String search, Pageable pageable) {
        Page<SkillListing> skills;
        
        if (search != null && !search.isEmpty()) {
            skills = skillListingRepository.findByTitreContainingIgnoreCase(search, pageable);
        } else {
            skills = skillListingRepository.findAll(pageable);
        }
        
        return skills.map(this::convertToDTO);
    }
    
    /**
     * Récupérer une compétence par ID
     */
    public SkillDetailDTO getSkillById(Long id) {
        SkillListing skill = skillListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence", "id", id));
        
        return convertToDetailDTO(skill);
    }
    
    /**
     * Mettre à jour une compétence
     */
    public SkillListingDTO updateSkill(Long id, SkillListingDTO dto, String userEmail) {
        SkillListing skill = skillListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence", "id", id));
        
        // Vérifier que l'utilisateur est le propriétaire
        if (!skill.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier cette compétence");
        }
        
        skill.setTitre(dto.getTitre());
        skill.setDescription(dto.getDescription());
        skill.setDisponibilites(dto.getDisponibilites());
        skill.setCommentaireEchange(dto.getCommentaireEchange());
        skill.setActif(dto.getActif());
                if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                    skill.setImages(dto.getImages());
                }
        
        SkillListing updatedSkill = skillListingRepository.save(skill);
        
        return convertToDTO(updatedSkill);
    }
    
    /**
     * Supprimer une compétence
     */
    public void deleteSkill(Long id, String userEmail) {
        SkillListing skill = skillListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétence", "id", id));
        
        // Vérifier que l'utilisateur est le propriétaire
        if (!skill.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à supprimer cette compétence");
        }
        
        skillListingRepository.delete(skill);
    }
    
    /**
     * Récupérer les compétences d'un utilisateur
     */
    public List<SkillListingDTO> getSkillsByOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", ownerEmail));
        
        List<SkillListing> skills = skillListingRepository.findByOwnerOrderByCreatedAtDesc(owner);
        
        return skills.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertir SkillListing en SkillListingDTO
     */
    private SkillListingDTO convertToDTO(SkillListing skill) {
        SkillListingDTO dto = new SkillListingDTO();
        dto.setId(skill.getId());
        dto.setTitre(skill.getTitre());
        dto.setDescription(skill.getDescription());
        dto.setDisponibilites(skill.getDisponibilites());
        dto.setCommentaireEchange(skill.getCommentaireEchange());
        dto.setActif(skill.getActif());
            dto.setImages(skill.getImages());
        dto.setOwnerId(skill.getOwner().getId());
        dto.setOwnerNom(skill.getOwner().getNom());
        dto.setOwnerPhoto(skill.getOwner().getPhoto());
        dto.setOwnerLocalisation(skill.getOwner().getLocalisation());
        dto.setOwnerRating(reviewRepository.getAverageRatingByUserId(skill.getOwner().getId()));
        dto.setCreatedAt(skill.getCreatedAt());
        dto.setUpdatedAt(skill.getUpdatedAt());
        
        return dto;
    }
    
    /**
     * Convertir SkillListing en SkillDetailDTO
     */
    private SkillDetailDTO convertToDetailDTO(SkillListing skill) {
        SkillDetailDTO dto = new SkillDetailDTO();
            dto.setImages(skill.getImages());
        dto.setId(skill.getId());
        dto.setTitre(skill.getTitre());
        dto.setDescription(skill.getDescription());
        dto.setDisponibilites(skill.getDisponibilites());
        dto.setCommentaireEchange(skill.getCommentaireEchange());
        dto.setActif(skill.getActif());
        dto.setOwnerId(skill.getOwner().getId());
        dto.setOwnerNom(skill.getOwner().getNom());
        dto.setOwnerPhoto(skill.getOwner().getPhoto());
        dto.setOwnerLocalisation(skill.getOwner().getLocalisation());
        dto.setOwnerRating(reviewRepository.getAverageRatingByUserId(skill.getOwner().getId()));
        dto.setCreatedAt(skill.getCreatedAt());
        dto.setUpdatedAt(skill.getUpdatedAt());
        
        // Owner complet
        UserDTO ownerDTO = new UserDTO();
        ownerDTO.setId(skill.getOwner().getId());
        ownerDTO.setEmail(skill.getOwner().getEmail());
        ownerDTO.setNom(skill.getOwner().getNom());
        ownerDTO.setLocalisation(skill.getOwner().getLocalisation());
        ownerDTO.setPhoto(skill.getOwner().getPhoto());
        ownerDTO.setBio(skill.getOwner().getBio());
        ownerDTO.setAverageRating(reviewRepository.getAverageRatingByUserId(skill.getOwner().getId()));
        
        dto.setOwner(ownerDTO);
        
        return dto;
    }
}