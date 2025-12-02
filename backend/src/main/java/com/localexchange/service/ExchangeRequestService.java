package com.localexchange.service;

import com.localexchange.dto.ExchangeRequestDTO;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.*;
import com.localexchange.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExchangeRequestService {
    
    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemListingRepository itemListingRepository;
    
    @Autowired
    private SkillListingRepository skillListingRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Créer une demande d'échange
     */
    public ExchangeRequestDTO createRequest(ExchangeRequestDTO dto, String beneficiaireEmail) {
        User beneficiaire = userRepository.findByEmail(beneficiaireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", beneficiaireEmail));
        
        ExchangeRequest request = new ExchangeRequest();
        request.setOffreEnRetour(dto.getOffreEnRetour());
        request.setDateEchange(dto.getDateEchange());
        request.setMessageInitial(dto.getMessageInitial());
        request.setStatut(ExchangeStatus.PENDING);
        request.setBeneficiaire(beneficiaire);
        
        // Déterminer si c'est pour un item ou une skill
        if (dto.getItemListingId() != null) {
            ItemListing item = itemListingRepository.findById(dto.getItemListingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Objet", "id", dto.getItemListingId()));
            request.setItemListing(item);
            request.setDonateur(item.getOwner());
        } else if (dto.getSkillListingId() != null) {
            SkillListing skill = skillListingRepository.findById(dto.getSkillListingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Compétence", "id", dto.getSkillListingId()));
            request.setSkillListing(skill);
            request.setDonateur(skill.getOwner());
        } else {
            throw new IllegalArgumentException("Une demande doit concerner un objet ou une compétence");
        }
        
        ExchangeRequest savedRequest = exchangeRequestRepository.save(request);
        
        // Créer notification pour le donateur
        String message = String.format("%s souhaite échanger avec vous", beneficiaire.getNom());
        notificationService.createNotification(
                savedRequest.getDonateur().getId(),
                NotificationType.NEW_REQUEST,
                message,
                savedRequest.getId(),
                savedRequest.getItemListing() != null ? savedRequest.getItemListing().getId() : null,
                savedRequest.getSkillListing() != null ? savedRequest.getSkillListing().getId() : null
        );
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Récupérer les demandes reçues (où je suis donateur)
     */
    public List<ExchangeRequestDTO> getReceivedRequests(String donateurEmail) {
        User donateur = userRepository.findByEmail(donateurEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", donateurEmail));
        
        List<ExchangeRequest> requests = exchangeRequestRepository.findByDonateurOrderByCreatedAtDesc(donateur);
        
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les demandes envoyées (où je suis bénéficiaire)
     */
    public List<ExchangeRequestDTO> getSentRequests(String beneficiaireEmail) {
        User beneficiaire = userRepository.findByEmail(beneficiaireEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", beneficiaireEmail));
        
        List<ExchangeRequest> requests = exchangeRequestRepository.findByBeneficiaireOrderByCreatedAtDesc(beneficiaire);
        
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer un échange par ID
     */
    public ExchangeRequestDTO getExchangeById(Long requestId, String userEmail) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'échange", "id", requestId));
        
        // Vérifier que l'utilisateur est impliqué dans l'échange (donateur ou bénéficiaire)
        if (!request.getDonateur().getEmail().equals(userEmail) && 
            !request.getBeneficiaire().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Demande d'échange", "id", requestId);
        }
        
        return convertToDTO(request);
    }
    
    /**
     * Accepter une demande d'échange
     */
    public ExchangeRequestDTO acceptRequest(Long requestId, String donateurEmail) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'échange", "id", requestId));
        
        // Vérifier que l'utilisateur est le donateur
        if (!request.getDonateur().getEmail().equals(donateurEmail)) {
            throw new UnauthorizedException("Seul le donateur peut accepter cette demande");
        }
        
        // Vérifier que la demande est en attente
        if (request.getStatut() != ExchangeStatus.PENDING) {
            throw new IllegalStateException("Cette demande ne peut plus être acceptée");
        }
        
        request.setStatut(ExchangeStatus.ACCEPTED);
        ExchangeRequest updatedRequest = exchangeRequestRepository.save(request);
        
        // Créer notification pour le bénéficiaire
        String message = String.format("%s a accepté votre demande d'échange", request.getDonateur().getNom());
        notificationService.createNotification(
                request.getBeneficiaire().getId(),
                NotificationType.REQUEST_ACCEPTED,
                message,
                request.getId(),
                request.getItemListing() != null ? request.getItemListing().getId() : null,
                request.getSkillListing() != null ? request.getSkillListing().getId() : null
        );
        
        return convertToDTO(updatedRequest);
    }
    
    /**
     * Refuser une demande d'échange
     */
    public ExchangeRequestDTO refuseRequest(Long requestId, String donateurEmail) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'échange", "id", requestId));
        
        // Vérifier que l'utilisateur est le donateur
        if (!request.getDonateur().getEmail().equals(donateurEmail)) {
            throw new UnauthorizedException("Seul le donateur peut refuser cette demande");
        }
        
        // Vérifier que la demande est en attente
        if (request.getStatut() != ExchangeStatus.PENDING) {
            throw new IllegalStateException("Cette demande ne peut plus être refusée");
        }
        
        request.setStatut(ExchangeStatus.REFUSED);
        ExchangeRequest updatedRequest = exchangeRequestRepository.save(request);
        
        // Créer notification pour le bénéficiaire
        String message = String.format("%s a refusé votre demande d'échange", request.getDonateur().getNom());
        notificationService.createNotification(
                request.getBeneficiaire().getId(),
                NotificationType.REQUEST_REFUSED,
                message,
                request.getId(),
                request.getItemListing() != null ? request.getItemListing().getId() : null,
                request.getSkillListing() != null ? request.getSkillListing().getId() : null
        );
        
        return convertToDTO(updatedRequest);
    }
    
    /**
     * Marquer un échange comme terminé
     */
    public ExchangeRequestDTO completeExchange(Long requestId, String userEmail) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'échange", "id", requestId));
        
        // Vérifier que l'utilisateur est impliqué dans l'échange
        if (!request.getDonateur().getEmail().equals(userEmail) && 
            !request.getBeneficiaire().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier cet échange");
        }
        
        // Vérifier que la demande est acceptée
        if (request.getStatut() != ExchangeStatus.ACCEPTED) {
            throw new IllegalStateException("Seuls les échanges acceptés peuvent être marqués comme terminés");
        }
        
        request.setStatut(ExchangeStatus.COMPLETED);
        ExchangeRequest updatedRequest = exchangeRequestRepository.save(request);
        
        return convertToDTO(updatedRequest);
    }
    
    /**
     * Annuler une demande d'échange
     */
    public ExchangeRequestDTO cancelRequest(Long requestId, String userEmail) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande d'échange", "id", requestId));
        
        // Vérifier que l'utilisateur est le bénéficiaire
        if (!request.getBeneficiaire().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Seul le demandeur peut annuler cette demande");
        }
        
        request.setStatut(ExchangeStatus.CANCELLED);
        ExchangeRequest updatedRequest = exchangeRequestRepository.save(request);
        
        return convertToDTO(updatedRequest);
    }
    
    /**
     * Convertir ExchangeRequest en ExchangeRequestDTO
     */
    private ExchangeRequestDTO convertToDTO(ExchangeRequest request) {
        ExchangeRequestDTO dto = new ExchangeRequestDTO();
        dto.setId(request.getId());
        dto.setOffreEnRetour(request.getOffreEnRetour());
        dto.setDateEchange(request.getDateEchange());
        dto.setStatut(request.getStatut().name());
        dto.setMessageInitial(request.getMessageInitial());
        
        dto.setBeneficiaireId(request.getBeneficiaire().getId());
        dto.setBeneficiaireNom(request.getBeneficiaire().getNom());
        dto.setBeneficiairePhoto(request.getBeneficiaire().getPhoto());
        
        dto.setDonateurId(request.getDonateur().getId());
        dto.setDonateurNom(request.getDonateur().getNom());
        dto.setDonateurPhoto(request.getDonateur().getPhoto());
        
        if (request.getItemListing() != null) {
            dto.setItemListingId(request.getItemListing().getId());
            dto.setItemTitre(request.getItemListing().getTitre());
        }
        
        if (request.getSkillListing() != null) {
            dto.setSkillListingId(request.getSkillListing().getId());
            dto.setSkillTitre(request.getSkillListing().getTitre());
        }
        
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        
        return dto;
    }
}