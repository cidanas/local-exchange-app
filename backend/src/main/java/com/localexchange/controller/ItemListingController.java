package com.localexchange.controller;

import com.localexchange.dto.ItemDetailDTO;
import com.localexchange.dto.ItemListingDTO;
import com.localexchange.service.ItemListingService;
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
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemListingController {
    
    @Autowired
    private ItemListingService itemListingService;
    
    /**
     * Créer une nouvelle annonce d'objet
     */
    @PostMapping
    public ResponseEntity<?> createItem(
            @Valid @RequestBody ItemListingDTO itemListingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ItemListingDTO createdItem = itemListingService.createItem(itemListingDTO, userDetails.getUsername());
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }
    
    /**
     * Récupérer tous les objets avec filtres et pagination
     */
    @GetMapping
    public ResponseEntity<?> getAllItems(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemListingDTO> items = itemListingService.getAllItems(categorie, search, pageable);
        return ResponseEntity.ok(items);
    }
    
    /**
     * Récupérer un objet par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        ItemDetailDTO item = itemListingService.getItemById(id);
        return ResponseEntity.ok(item);
    }
    
    /**
     * Mettre à jour un objet
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemListingDTO itemListingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ItemListingDTO updatedItem = itemListingService.updateItem(id, itemListingDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedItem);
    }
    
    /**
     * Supprimer un objet
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        itemListingService.deleteItem(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Récupérer les objets de l'utilisateur connecté
     */
    @GetMapping("/my-items")
    public ResponseEntity<?> getMyItems(@AuthenticationPrincipal UserDetails userDetails) {
        List<ItemListingDTO> items = itemListingService.getItemsByOwner(userDetails.getUsername());
        return ResponseEntity.ok(items);
    }
}