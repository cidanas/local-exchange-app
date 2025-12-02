package com.localexchange.repository;

import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.ExchangeStatus;
import com.localexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    
    List<ExchangeRequest> findByDonateurOrderByCreatedAtDesc(User donateur);
    
    List<ExchangeRequest> findByBeneficiaireOrderByCreatedAtDesc(User beneficiaire);
    
    List<ExchangeRequest> findByStatut(ExchangeStatus statut);
    
    List<ExchangeRequest> findByDonateurAndStatut(User donateur, ExchangeStatus statut);
    
    List<ExchangeRequest> findByBeneficiaireAndStatut(User beneficiaire, ExchangeStatus statut);
    
    Optional<ExchangeRequest> findByIdAndDonateur(Long id, User donateur);
    
    Long countByBeneficiaireAndStatut(User beneficiaire, ExchangeStatus statut);
    
    Long countByDonateurAndStatut(User donateur, ExchangeStatus statut);
}