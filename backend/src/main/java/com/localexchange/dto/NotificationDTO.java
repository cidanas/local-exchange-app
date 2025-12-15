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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Long getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Long exchangeId) {
		this.exchangeId = exchangeId;
	}
	public Long getItemListingId() {
		return itemListingId;
	}
	public void setItemListingId(Long itemListingId) {
		this.itemListingId = itemListingId;
	}
	public Long getSkillListingId() {
		return skillListingId;
	}
	public void setSkillListingId(Long skillListingId) {
		this.skillListingId = skillListingId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    
    
}