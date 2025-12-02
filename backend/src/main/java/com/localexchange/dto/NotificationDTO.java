package com.localexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    
    private Long id;
    private String type;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
    private Long exchangeId;
    private Long itemListingId;
    private Long skillListingId;
    private String title;
}