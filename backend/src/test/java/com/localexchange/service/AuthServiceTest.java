package com.localexchange.service;

import com.localexchange.dto.RegisterDTO;
import com.localexchange.dto.UserDTO;
import com.localexchange.exception.DuplicateEmailException;
import com.localexchange.repository.*;
import com.localexchange.security.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceTest{
	
	@Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;
    
    @BeforeEach
    public void setUp(){
    	MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void Registertest() {
		
		//Tester si l'on empeche bien la creation avec le mail email
		
		
		RegisterDTO regisTest = new RegisterDTO();
		regisTest.setEmail("email@mail.fr");
		regisTest.setPassword("mdp");
		regisTest.setNom("Bonjour");
		regisTest.setLocalisation("Pau");
		
		when(userRepository.existsByEmail("email@mail.fr")).thenReturn(true);

		assertThrows(DuplicateEmailException.class , () -> authService.register(regisTest),"On ne devrais pas pouvoir créer deux comptes avec la même adresse");
	}
	
	
}