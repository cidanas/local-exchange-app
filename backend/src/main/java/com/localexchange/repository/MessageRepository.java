package com.localexchange.repository;

import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.Message;
import com.localexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByExchangeRequestOrderByCreatedAtAsc(ExchangeRequest exchangeRequest);
    
    Integer countByDestinataireAndLuFalse(User destinataire);
    
    List<Message> findByDestinataireAndLuFalse(User destinataire);
}