package com.localexchange.controller;

import com.localexchange.dto.SkillDetailDTO;
import com.localexchange.dto.SkillListingDTO;
import com.localexchange.service.SkillListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:5173")
public class SkillListingController {
    
    @Autowired
    private SkillListingService skillListingService;
    
    /**
     * Créer une nouvelle annonce de compétence
     */
    @PostMapping
    public ResponseEntity<?> createSkill(
            @Valid @RequestBody SkillListingDTO skillListingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        SkillListingDTO createdSkill = skillListingService.createSkill(skillListingDTO, userDetails.getUsername());
        return new ResponseEntity<>(createdSkill, HttpStatus.CREATED);
    }
    
    /**
     * Récupérer toutes les compétences avec recherche et pagination
     */
    @GetMapping
    public ResponseEntity<?> getAllSkills(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SkillListingDTO> skills = skillListingService.getAllSkills(search, pageable);
        return ResponseEntity.ok(skills);
    }
    
    /**
     * Récupérer une compétence par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillById(@PathVariable Long id) {
        SkillDetailDTO skill = skillListingService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }
    
    /**
     * Mettre à jour une compétence
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillListingDTO skillListingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        SkillListingDTO updatedSkill = skillListingService.updateSkill(id, skillListingDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedSkill);
    }
    
    /**
     * Supprimer une compétence
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        skillListingService.deleteSkill(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Récupérer les compétences de l'utilisateur connecté
     */
    @GetMapping("/my-skills")
    public ResponseEntity<?> getMySkills(@AuthenticationPrincipal UserDetails userDetails) {
        List<SkillListingDTO> skills = skillListingService.getSkillsByOwner(userDetails.getUsername());
        return ResponseEntity.ok(skills);
    }
}