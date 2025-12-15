package com.localexchange.service;

import com.localexchange.dto.LoginDTO;
import com.localexchange.dto.LoginResponseDTO;
import com.localexchange.dto.ProfileUpdateDTO;
import com.localexchange.dto.RegisterDTO;
import com.localexchange.dto.UserDTO;
import com.localexchange.exception.DuplicateEmailException;
import com.localexchange.exception.InvalidCredentialsException;
import com.localexchange.exception.ResourceNotFoundException;
import com.localexchange.model.User;
import com.localexchange.repository.*;
import com.localexchange.security.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class AuthServiceTest{
	
	@Mock
    private UserRepository mockUserRepository;

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private JwtTokenProvider mockTokenProvider;
    
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    

    @InjectMocks
    private AuthService authService;
    
    @BeforeEach
    public void setUp(){
    	MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void Registertest() {
		
		RegisterDTO regisTest = new RegisterDTO();
		regisTest.setEmail("email@mail.fr");
		regisTest.setPassword("mdp");
		regisTest.setNom("Bonjour");
		regisTest.setLocalisation("Pau");
		
		when(mockUserRepository.existsByEmail("email@mail.fr")).thenReturn(true);
		
		
		//Tester si l'on empeche bien la creation avec le meme email
		assertThrows(DuplicateEmailException.class , () -> authService.register(regisTest),"On ne devrais pas pouvoir créer deux comptes avec la même adresse");
		
	}
	
	@Test
	public void loginTest(){
		
		
		//Setup mocks
		RegisterDTO regisTest = new RegisterDTO();
		regisTest.setEmail("email@mail.fr");
		regisTest.setPassword("mdp");
		regisTest.setNom("Bonjour");
		regisTest.setLocalisation("Pau");
		
		when(mockTokenProvider.generateToken(any(UserDetails.class))).thenReturn("Mock Tocken");

		UserDetails usDeTest = org.springframework.security.core.userdetails.User.withUsername("email@mail.fr").password("encodedPassword").authorities("USER").build();
		
	    Authentication authentication = mock(Authentication.class);
	    
	    when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
	    
		when(authentication.getPrincipal()).thenReturn(usDeTest);
        
        User userTest = new User();
        userTest.setId(123456L);
		userTest.setEmail(regisTest.getEmail());
        userTest.setNom(regisTest.getNom());
        userTest.setLocalisation(regisTest.getLocalisation());
        userTest.setPhoneVerified(false);
        
        
        when(mockReviewRepository.getAverageRatingByUserId(any(Long.class))).thenReturn(Double.valueOf(4));
        when(mockUserRepository.findByEmail("email@mail.fr")).thenReturn(Optional.of(userTest));
		
		LoginDTO loginTest = new LoginDTO();
		loginTest.setEmail("email@mail.fr");
		loginTest.setPassword("mdp");
		
		
		//Tests
		LoginResponseDTO lgTest = authService.login(loginTest);
		
		assert(lgTest.getUser().getEmail().equals(userTest.getEmail())) : "On ne récupère pas la bonne adresse mail";
		assert(lgTest.getUser().getNom().equals(userTest.getNom())) : "On ne récupère pas le bon nom";
		assert(lgTest.getUser().getLocalisation().equals(userTest.getLocalisation())) : "On ne récupère pas la bonne adresse localisation";
		
		assert(lgTest.getToken().equals("Mock Tocken")) : "On ne recupère pas le bon token";

		
		LoginDTO fauxloginTest = new LoginDTO();
		loginTest.setEmail("faux@mail.fr");
		loginTest.setPassword("mdp");
		
		
		when(mockUserRepository.findByEmail("faux@mail.fr")).thenThrow(new ResourceNotFoundException("Utilisateur", "email", fauxloginTest.getEmail()));
		
		assertThrows(InvalidCredentialsException.class, () -> authService.login(fauxloginTest),"Mauvaise erreur ou récupération d'une mauvaise adresse");
		
	}
	
	@Test
	public void updateProfilTest(){
		
		User userTest = new User();
        userTest.setId(123456L);
		userTest.setEmail("email@mail.fr");
        userTest.setNom("nom");
        userTest.setLocalisation("Pau");
        userTest.setPhoneVerified(false);
        
        ProfileUpdateDTO puDTOtest = new ProfileUpdateDTO();
        puDTOtest.setNom("nouveau nom");
        
        when(mockUserRepository.findByEmail("email@mail.fr")).thenReturn(Optional.of(userTest));
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        assert(authService.updateProfile("email@mail.fr", puDTOtest).getNom().equals("nouveau nom")) : "mise a jour incorrecte" ;
        assert(authService.updateProfile("email@mail.fr", puDTOtest).getLocalisation().equals("Pau")) : "mise a jour d'un élément qui n'aurait pas du l'être";
        
	}
	
	//Les fonctions restantes sois contiennent une seule methode que l'on doit mocker, sois sont privées
	
	
}