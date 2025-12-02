package com.localexchange.service;

import com.localexchange.dto.MessageDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.Message;
import com.localexchange.model.NotificationType;
import com.localexchange.model.User;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.MessageRepository;
import com.localexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Envoyer un message
     */
    public MessageDTO sendMessage(MessageDTO dto, String expediteurEmail) {
        User expediteur = userRepository.findByEmail(expediteurEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", expediteurEmail));
        
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(dto.getExchangeRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Échange", "id", dto.getExchangeRequestId()));
        
        // Vérifier que l'utilisateur fait partie de l'échange
        if (!exchangeRequest.getDonateur().getId().equals(expediteur.getId()) &&
            !exchangeRequest.getBeneficiaire().getId().equals(expediteur.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à envoyer des messages dans cet échange");
        }
        
        // Déterminer le destinataire
        User destinataire = exchangeRequest.getDonateur().getId().equals(expediteur.getId())
                ? exchangeRequest.getBeneficiaire()
                : exchangeRequest.getDonateur();
        
        Message message = new Message();
        message.setContenu(dto.getContenu());
        message.setLu(false);
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);
        message.setExchangeRequest(exchangeRequest);
        
        Message savedMessage = messageRepository.save(message);
        
        // Créer notification pour le destinataire
        String notificationMessage = String.format("Nouveau message de %s", expediteur.getNom());
        notificationService.createNotification(
                destinataire.getId(),
                NotificationType.NEW_MESSAGE,
                notificationMessage,
                exchangeRequest.getId(),
                null,
                null
        );
        
        return convertToDTO(savedMessage);
    }
    
    /**
     * Récupérer la conversation d'un échange
     */
    public List<MessageDTO> getConversation(Long exchangeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
        
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findById(exchangeId)
                .orElseThrow(() -> new ResourceNotFoundException("Échange", "id", exchangeId));
        
        // Vérifier que l'utilisateur fait partie de l'échange
        if (!exchangeRequest.getDonateur().getId().equals(user.getId()) &&
            !exchangeRequest.getBeneficiaire().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à consulter cette conversation");
        }
        
        List<Message> messages = messageRepository.findByExchangeRequestOrderByCreatedAtAsc(exchangeRequest);
        
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Marquer un message comme lu
     */
    public MessageDTO markAsRead(Long messageId, String userEmail) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
        
        // Vérifier que l'utilisateur est le destinataire
        if (!message.getDestinataire().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'êtes pas le destinataire de ce message");
        }
        
        message.setLu(true);
        Message updatedMessage = messageRepository.save(message);
        
        return convertToDTO(updatedMessage);
    }
    
    /**
     * Convertir Message en MessageDTO
     */
    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContenu(message.getContenu());
        dto.setLu(message.getLu());
        dto.setExpediteurId(message.getExpediteur().getId());
        dto.setExpediteurNom(message.getExpediteur().getNom());
        dto.setExpediteurPhoto(message.getExpediteur().getPhoto());
        dto.setDestinataireId(message.getDestinataire().getId());
        dto.setDestinataireNom(message.getDestinataire().getNom());
        dto.setDestinatairePhoto(message.getDestinataire().getPhoto());
        dto.setExchangeRequestId(message.getExchangeRequest().getId());
        dto.setCreatedAt(message.getCreatedAt());
        
        return dto;
    }
}