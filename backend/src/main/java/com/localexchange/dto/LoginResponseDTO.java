package com.localexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
   
	private String token;
   
	private UserDTO user;
    
    public LoginResponseDTO(String token2, UserDTO userDTO) {
		this.token = token2;
		this.user = userDTO;
	}
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

    
}