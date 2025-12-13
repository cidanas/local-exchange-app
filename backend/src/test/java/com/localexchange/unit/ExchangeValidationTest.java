package com.localexchange.unit;

import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires - Validation des demandes d'échange
 */
public class ExchangeValidationTest {

    @Test
    public void testValidExchangeRequest() {
        ExchangeRequest exchange = new ExchangeRequest();
        User donateur = new User();
        User beneficiaire = new User();
        
        donateur.setId(1L);
        beneficiaire.setId(2L);

        exchange.setDonateur(donateur);
        exchange.setBeneficiaire(beneficiaire);
        exchange.setOffreEnRetour("Book for Pen");
        exchange.setDateEchange(LocalDate.now().plusDays(7));

        assertNotNull(exchange.getDonateur());
        assertNotNull(exchange.getBeneficiaire());
        assertNotNull(exchange.getOffreEnRetour());
        assertNotNull(exchange.getDateEchange());
    }

    @Test
    public void testExchangeWithPastDate() {
        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setDateEchange(LocalDate.now().minusDays(1));

        assertTrue(exchange.getDateEchange().isBefore(LocalDate.now()));
    }

    @Test
    public void testExchangeSameDonatorBeneficiary() {
        User user = new User();
        user.setId(1L);

        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setDonateur(user);
        exchange.setBeneficiaire(user);

        assertEquals(exchange.getDonateur().getId(), exchange.getBeneficiaire().getId());
        assertTrue(exchange.getDonateur().getId().equals(exchange.getBeneficiaire().getId()));
    }

    @Test
    public void testExchangeOfferNotBlank() {
        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setOffreEnRetour("Item A for Item B");

        assertNotNull(exchange.getOffreEnRetour());
        assertFalse(exchange.getOffreEnRetour().isEmpty());
        assertTrue(exchange.getOffreEnRetour().length() > 0);
    }
}
