package com.localexchange.controller;

import com.localexchange.dto.ExchangeRequestDTO;
import com.localexchange.service.ExchangeRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchanges")
@CrossOrigin(origins = "http://localhost:5173")
public class ExchangeRequestController {
    
    @Autowired
    private ExchangeRequestService exchangeRequestService;
    
    /**
     * Créer une nouvelle demande d'échange
     */
    @PostMapping
    public ResponseEntity<?> createRequest(
            @Valid @RequestBody ExchangeRequestDTO exchangeRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ExchangeRequestDTO createdRequest = exchangeRequestService.createRequest(
                exchangeRequestDTO, 
                userDetails.getUsername()
        );
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }
    
    /**
     * Récupérer les demandes reçues (où je suis donateur)
     */
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedRequests(@AuthenticationPrincipal UserDetails userDetails) {
        List<ExchangeRequestDTO> requests = exchangeRequestService.getReceivedRequests(userDetails.getUsername());
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Récupérer les demandes envoyées (où je suis bénéficiaire)
     */
    @GetMapping("/sent")
    public ResponseEntity<?> getSentRequests(@AuthenticationPrincipal UserDetails userDetails) {
        List<ExchangeRequestDTO> requests = exchangeRequestService.getSentRequests(userDetails.getUsername());
        return ResponseEntity.ok(requests);
    }
    
    /**
     * Récupérer un échange par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getExchangeById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ExchangeRequestDTO request = exchangeRequestService.getExchangeById(id, userDetails.getUsername());
        return ResponseEntity.ok(request);
    }
    
    /**
     * Accepter une demande d'échange
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ExchangeRequestDTO acceptedRequest = exchangeRequestService.acceptRequest(id, userDetails.getUsername());
        return ResponseEntity.ok(acceptedRequest);
    }
    
    /**
     * Refuser une demande d'échange
     */
    @PutMapping("/{id}/refuse")
    public ResponseEntity<?> refuseRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ExchangeRequestDTO refusedRequest = exchangeRequestService.refuseRequest(id, userDetails.getUsername());
        return ResponseEntity.ok(refusedRequest);
    }
    
    /**
     * Marquer un échange comme terminé
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeExchange(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ExchangeRequestDTO completedRequest = exchangeRequestService.completeExchange(id, userDetails.getUsername());
        return ResponseEntity.ok(completedRequest);
    }
    
    /**
     * Annuler une demande d'échange
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ExchangeRequestDTO cancelledRequest = exchangeRequestService.cancelRequest(id, userDetails.getUsername());
        return ResponseEntity.ok(cancelledRequest);
    }
}