package com.localexchange.repository;

import com.localexchange.model.Notification;
import com.localexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    
    Integer countByUserAndLuFalse(User user);
    
    List<Notification> findByUserAndLuFalse(User user);
}