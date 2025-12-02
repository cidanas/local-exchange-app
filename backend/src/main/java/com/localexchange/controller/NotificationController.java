package com.localexchange.controller;

import com.localexchange.dto.NotificationDTO;
import com.localexchange.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Récupérer toutes les notifications de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<?> getMyNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userDetails.getUsername());
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Récupérer le nombre de notifications non lues
     */
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        Integer count = notificationService.getUnreadCount(userDetails.getUsername());
        
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        NotificationDTO notification = notificationService.markAsRead(id, userDetails.getUsername());
        return ResponseEntity.ok(notification);
    }
    
    /**
     * Marquer toutes les notifications comme lues
     */
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getUsername());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Toutes les notifications ont été marquées comme lues");
        
        return ResponseEntity.ok(response);
    }
}