package com.localexchange.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.localexchange.model.ItemListing;
import com.localexchange.model.User;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.ItemListingRepository;
import com.localexchange.repository.SkillListingRepository;
import com.localexchange.repository.UserRepository;
import com.localexchange.dto.ExchangeRequestDTO;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.*;
import com.localexchange.repository.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
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

public class ExchangeRequestServiceTest{
	
    @Mock
    private ExchangeRequestRepository mockExchangeRequestRepository;
    
    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private ItemListingRepository mockItemListingRepository;
    
    @Mock
    private SkillListingRepository mockSkillListingRepository;
    
    @Mock
    private NotificationService mockNotificationService;
    
    @InjectMocks
    private ExchangeRequestService exchangeRequestService;
    
    @BeforeEach
    public void setUp(){
    	MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    public void createRequestTest(){
    	
    	User beneficiaire = new User();
    	beneficiaire.setId(987L);
    	beneficiaire.setEmail("benef@mail.fr");
    	beneficiaire.setNom("nom");
    	beneficiaire.setLocalisation("Pau");
    	beneficiaire.setPhoneVerified(false);
    	
    	User owner = new User();
    	owner.setId(123456L);
    	owner.setEmail("owner@mail.fr");
    	owner.setNom("nom");
    	owner.setLocalisation("Pau");
    	owner.setPhoneVerified(false);
    	
        
        when(mockUserRepository.findByEmail("benef@mail.fr")).thenReturn(Optional.of(beneficiaire));
        
        
        ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente.");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(owner);
		
		when(mockItemListingRepository.findById(any(Long.class))).thenReturn(Optional.of(itTest));
		
		SkillListing skTest = new SkillListing();
    	skTest.setId(1111L);
		skTest.setTitre("Skill");
		skTest.setDescription("Un skill proposé.");
		skTest.setCommentaireEchange("Bonjour");
		skTest.setOwner(owner);
		
		when(mockSkillListingRepository.findById(any(Long.class))).thenReturn(Optional.of(skTest));
		
		when(mockExchangeRequestRepository.save(any(ExchangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ExchangeRequestDTO erTest = new ExchangeRequestDTO();
		erTest.setOffreEnRetour("Un autre objet");
		erTest.setDateEchange(LocalDate.now());
		erTest.setMessageInitial("Je voudrais echanger");
		erTest.setItemListingId(0000L);
		erTest.setDonateurId(123456L);
		
		ExchangeRequestDTO exchangeITtest =exchangeRequestService.createRequest(erTest, "benef@mail.fr");
		
		erTest.setItemListingId(null);
		erTest.setSkillListingId(1111L);
		ExchangeRequestDTO exchangeSKtest = exchangeRequestService.createRequest(erTest, "benef@mail.fr");
		
		assert(exchangeITtest.getItemListingId() != null) : "Devrait avoir un ItemListingId non null" ;
		assert(exchangeITtest.getSkillListingId() == null) : "Devrait avoir un SkillListingId null" ;
		
		assert(exchangeSKtest.getSkillListingId() != null) : "Devrait avoir un SkillListingId non null" ;
		assert(exchangeSKtest.getItemListingId() == null) : "Devrait avoir un ItemListingId null" ;
		
		erTest.setSkillListingId(null);
		
		assertThrows(IllegalArgumentException.class, () -> exchangeRequestService.createRequest(erTest, "benef@mail.fr"), "Ne devrait pas accepter sans SkillListingId ou ItemListingId" );
		
    }
	
    @Test
    public void acceptRequestTest(){
    	
    	User beneficiaire = new User();
    	beneficiaire.setId(987L);
    	beneficiaire.setEmail("benef@mail.fr");
    	beneficiaire.setNom("nom");
    	beneficiaire.setLocalisation("Pau");
    	beneficiaire.setPhoneVerified(false);
    	
    	User owner = new User();
    	owner.setId(123456L);
    	owner.setEmail("owner@mail.fr");
    	owner.setNom("nom");
    	owner.setLocalisation("Pau");
    	owner.setPhoneVerified(false);
    	
    	ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente.");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(owner);
    	
    	ExchangeRequest erTest = new ExchangeRequest();
    	erTest.setId(2222L);
		erTest.setOffreEnRetour("Un autre objet");
		erTest.setDateEchange(LocalDate.now());
		erTest.setMessageInitial("Je voudrais echanger");
		erTest.setItemListing(itTest);
		erTest.setDonateur(owner);
		erTest.setBeneficiaire(beneficiaire);
		
		when(mockExchangeRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(erTest));
		
		when(mockExchangeRequestRepository.save(any(ExchangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ExchangeRequestDTO exchangetest = exchangeRequestService.acceptRequest(0000L, "owner@mail.fr");
		
		assert(exchangetest.getStatut() == "ACCEPTED" ) : "L'echange devrait être accepté";
		assertThrows(UnauthorizedException.class, () -> exchangeRequestService.acceptRequest(0000L, "booh@mail.fr"),"On ne doit pas pouvoir accepter à la place de quelqu'un d'autre");
		
		erTest.setStatut(ExchangeStatus.CANCELLED);
		assertThrows(IllegalStateException.class, () -> exchangeRequestService.acceptRequest(0000L, "owner@mail.fr"),"On ne doit pas pouvoir accepter la demande si elle n'est plus en attente");
    }
    
    @Test
    public void refusedRequestTest(){
    	
    	User beneficiaire = new User();
    	beneficiaire.setId(987L);
    	beneficiaire.setEmail("benef@mail.fr");
    	beneficiaire.setNom("nom");
    	beneficiaire.setLocalisation("Pau");
    	beneficiaire.setPhoneVerified(false);
    	
    	User owner = new User();
    	owner.setId(123456L);
    	owner.setEmail("owner@mail.fr");
    	owner.setNom("nom");
    	owner.setLocalisation("Pau");
    	owner.setPhoneVerified(false);
    	
    	ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente.");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(owner);
    	
    	ExchangeRequest erTest = new ExchangeRequest();
    	erTest.setId(2222L);
		erTest.setOffreEnRetour("Un autre objet");
		erTest.setDateEchange(LocalDate.now());
		erTest.setMessageInitial("Je voudrais echanger");
		erTest.setItemListing(itTest);
		erTest.setDonateur(owner);
		erTest.setBeneficiaire(beneficiaire);
		
		when(mockExchangeRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(erTest));
		
		when(mockExchangeRequestRepository.save(any(ExchangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ExchangeRequestDTO exchangetest = exchangeRequestService.refuseRequest(0000L, "owner@mail.fr");
		
		assert(exchangetest.getStatut() == "REFUSED" ) : "L'echange devrait être refusé";
		assertThrows(UnauthorizedException.class, () -> exchangeRequestService.refuseRequest(0000L, "booh@mail.fr"),"On ne doit pas pouvoir refuser à la place de quelqu'un d'autre");
		
		erTest.setStatut(ExchangeStatus.ACCEPTED);
		assertThrows(IllegalStateException.class, () -> exchangeRequestService.refuseRequest(0000L, "owner@mail.fr"),"On ne doit pas pouvoir refuser la demande si elle n'est plus en attente");
    }
    
    
    @Test
    public void completeExchangeTest(){
    	
    	User beneficiaire = new User();
    	beneficiaire.setId(987L);
    	beneficiaire.setEmail("benef@mail.fr");
    	beneficiaire.setNom("nom");
    	beneficiaire.setLocalisation("Pau");
    	beneficiaire.setPhoneVerified(false);
    	
    	User owner = new User();
    	owner.setId(123456L);
    	owner.setEmail("owner@mail.fr");
    	owner.setNom("nom");
    	owner.setLocalisation("Pau");
    	owner.setPhoneVerified(false);
    	
    	ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente.");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(owner);
    	
    	ExchangeRequest erTest = new ExchangeRequest();
    	erTest.setId(2222L);
		erTest.setOffreEnRetour("Un autre objet");
		erTest.setDateEchange(LocalDate.now());
		erTest.setMessageInitial("Je voudrais echanger");
		erTest.setItemListing(itTest);
		erTest.setDonateur(owner);
		erTest.setBeneficiaire(beneficiaire);
		erTest.setStatut(ExchangeStatus.ACCEPTED);
		
		when(mockExchangeRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(erTest));
		
		when(mockExchangeRequestRepository.save(any(ExchangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ExchangeRequestDTO exchangetest = exchangeRequestService.completeExchange(0000L, "owner@mail.fr");
		
		assert(exchangetest.getStatut() == "COMPLETED" ) : "L'echange devrait être complété";
		assertThrows(UnauthorizedException.class, () -> exchangeRequestService.completeExchange(0000L, "booh@mail.fr"),"On ne doit pas pouvoir completer à la place de quelqu'un d'autre");
		
		erTest.setStatut(ExchangeStatus.CANCELLED);
		assertThrows(IllegalStateException.class, () -> exchangeRequestService.completeExchange(0000L, "owner@mail.fr"),"On ne doit pas pouvoir completer la demande si elle n'est pas acceptée");
		
    }
    
    @Test
    public void cancelExchangeTest(){
    	
    	User beneficiaire = new User();
    	beneficiaire.setId(987L);
    	beneficiaire.setEmail("benef@mail.fr");
    	beneficiaire.setNom("nom");
    	beneficiaire.setLocalisation("Pau");
    	beneficiaire.setPhoneVerified(false);
    	
    	User owner = new User();
    	owner.setId(123456L);
    	owner.setEmail("owner@mail.fr");
    	owner.setNom("nom");
    	owner.setLocalisation("Pau");
    	owner.setPhoneVerified(false);
    	
    	ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente.");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(owner);
    	
    	ExchangeRequest erTest = new ExchangeRequest();
    	erTest.setId(2222L);
		erTest.setOffreEnRetour("Un autre objet");
		erTest.setDateEchange(LocalDate.now());
		erTest.setMessageInitial("Je voudrais echanger");
		erTest.setItemListing(itTest);
		erTest.setDonateur(owner);
		erTest.setBeneficiaire(beneficiaire);
		
		when(mockExchangeRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(erTest));
		
		when(mockExchangeRequestRepository.save(any(ExchangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		ExchangeRequestDTO exchangetest = exchangeRequestService.cancelRequest(0000L, "benef@mail.fr");
		
		assert(exchangetest.getStatut() == "CANCELLED" ) : "L'echange devrait être annulé";
		assertThrows(UnauthorizedException.class, () -> exchangeRequestService.cancelRequest(0000L, "booh@mail.fr"),"On ne doit pas pouvoir annuler à la place de quelqu'un d'autre");
    }
    
}