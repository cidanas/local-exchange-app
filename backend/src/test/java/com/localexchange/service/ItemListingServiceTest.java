package com.localexchange.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.localexchange.dto.ItemListingDTO;
import com.localexchange.exception.UnauthorizedException;
import com.localexchange.model.ItemListing;
import com.localexchange.model.User;
import com.localexchange.repository.ItemListingRepository;
import com.localexchange.repository.ReviewRepository;
import com.localexchange.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ItemListingServiceTest{
	
	@Mock
    private ItemListingRepository mockItemListingRepository;
    
	@Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private ReviewRepository mockReviewRepository;
    
    @InjectMocks
    private ItemListingService itemListingService;
    
    @BeforeEach
    public void setUp(){
    	MockitoAnnotations.openMocks(this);
    }
	
    @Test
	public void createItemTest(){
		
		ItemListingDTO itDTOtest = new ItemListingDTO();
		itDTOtest.setTitre("Objet");
		itDTOtest.setDescription("Un ojbet mis en vente");
		itDTOtest.setCategorie("Outils");
		itDTOtest.setCommentaireEchange("Bonjour");
		
		User userTest = new User();
        userTest.setId(123456L);
		userTest.setEmail("email@mail.fr");
        userTest.setNom("nom");
        userTest.setLocalisation("Pau");
        userTest.setPhoneVerified(false);
        
		
		when(mockUserRepository.findByEmail("email@mail.fr")).thenReturn(Optional.of(userTest));
		when(mockItemListingRepository.save(any(ItemListing.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		
		assert(itemListingService.createItem(itDTOtest,"email@mail.fr").getTitre() == "Objet") : "Titre incorrect";
		assert(itemListingService.createItem(itDTOtest,"email@mail.fr").getOwnerId() == 123456L) : "Utilisateur incorrect";
		
	}
	
    @Test
    public void updateItemTest(){
    	
    	User userTest = new User();
        userTest.setId(123456L);
		userTest.setEmail("email@mail.fr");
        userTest.setNom("nom");
        userTest.setLocalisation("Pau");
        userTest.setPhoneVerified(false);
    	
    	ItemListing itTest = new ItemListing();
    	itTest.setId(0000L);
		itTest.setTitre("Objet");
		itTest.setDescription("Un ojbet mis en vente");
		itTest.setCategorie("Outils");
		itTest.setCommentaireEchange("Bonjour");
		itTest.setOwner(userTest);
		
		ItemListingDTO itDTOtest = new ItemListingDTO();
		itDTOtest.setTitre("Objet");
		itDTOtest.setDescription("Nouvelle description");
		itDTOtest.setCategorie("Outils");
		itDTOtest.setCommentaireEchange("Bonjour");
		
		when(mockItemListingRepository.findById(any(Long.class))).thenReturn(Optional.of(itTest));
		when(mockItemListingRepository.save(any(ItemListing.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		assert(itemListingService.updateItem(0000L,itDTOtest , "email@mail.fr").getDescription().equals("Nouvelle description"));
		assertThrows(UnauthorizedException.class,() -> itemListingService.updateItem(0000L,itDTOtest , "email@m.fr"),"On peut modifier un Item avec la mauvaise adresse.");
    }
    
    
}