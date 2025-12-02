package com.localexchange.controller;

import com.localexchange.dto.MessageDTO;
import com.localexchange.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    /**
     * Envoyer un nouveau message
     */
    @PostMapping
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody MessageDTO messageDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        MessageDTO sentMessage = messageService.sendMessage(messageDTO, userDetails.getUsername());
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }
    
    /**
     * Récupérer la conversation d'un échange
     */
    @GetMapping("/conversation/{exchangeId}")
    public ResponseEntity<?> getConversation(
            @PathVariable Long exchangeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<MessageDTO> messages = messageService.getConversation(exchangeId, userDetails.getUsername());
        return ResponseEntity.ok(messages);
    }
    
    /**
     * Marquer un message comme lu
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        MessageDTO message = messageService.markAsRead(id, userDetails.getUsername());
        return ResponseEntity.ok(message);
    }
}