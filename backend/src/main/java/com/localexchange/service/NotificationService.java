package com.localexchange.service;

import com.localexchange.dto.NotificationDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.Notification;
import com.localexchange.model.NotificationType;
import com.localexchange.model.User;
import com.localexchange.repository.NotificationRepository;
import com.localexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class NotificationService {
    @Autowired
private NotificationRepository notificationRepository;

@Autowired
private UserRepository userRepository;

/**
 * Créer une notification
 */
public Notification createNotification(Long userId, NotificationType type, String message, Long exchangeId, Long itemListingId, Long skillListingId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userId));
    
    Notification notification = new Notification();
    notification.setType(type);
    notification.setMessage(message);
    notification.setLu(false);
    notification.setUser(user);
    notification.setExchangeId(exchangeId);
    notification.setItemListingId(itemListingId);
    notification.setSkillListingId(skillListingId);
    
    return notificationRepository.save(notification);
}

/**
 * Version surchargée (backward compatibility)
 */
public Notification createNotification(Long userId, NotificationType type, String message) {
    return createNotification(userId, type, message, null, null, null);
}

/**
 * Récupérer les notifications d'un utilisateur
 */
public List<NotificationDTO> getUserNotifications(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
    
    List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
    
    return notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}

/**
 * Compter les notifications non lues
 */
public Integer getUnreadCount(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
    
    return notificationRepository.countByUserAndLuFalse(user);
}

/**
 * Marquer une notification comme lue
 */
public NotificationDTO markAsRead(Long notificationId, String userEmail) {
    Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
    
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
    
    // Vérifier que la notification appartient à l'utilisateur
    if (!notification.getUser().getId().equals(user.getId())) {
        throw new UnauthorizedException("Cette notification ne vous appartient pas");
    }
    
    notification.setLu(true);
    Notification updatedNotification = notificationRepository.save(notification);
    
    return convertToDTO(updatedNotification);
}

/**
 * Marquer toutes les notifications comme lues
 */
public void markAllAsRead(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
    
    List<Notification> unreadNotifications = notificationRepository.findByUserAndLuFalse(user);
    
    unreadNotifications.forEach(notification -> notification.setLu(true));
    
    notificationRepository.saveAll(unreadNotifications);
}

/**
 * Convertir Notification en NotificationDTO
 */
private NotificationDTO convertToDTO(Notification notification) {
    NotificationDTO dto = new NotificationDTO();
    dto.setId(notification.getId());
    dto.setType(notification.getType().name());
    dto.setMessage(notification.getMessage());
    dto.setRead(notification.getLu());
    dto.setCreatedAt(notification.getCreatedAt());
    dto.setExchangeId(notification.getExchangeId());
    dto.setItemListingId(notification.getItemListingId());
    dto.setSkillListingId(notification.getSkillListingId());
    
    return dto;
}

}