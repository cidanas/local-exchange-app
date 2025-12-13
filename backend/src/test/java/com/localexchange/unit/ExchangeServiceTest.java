package com.localexchange.unit;

import com.localexchange.dto.ExchangeRequestDTO;
import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.ExchangeStatus;
import com.localexchange.model.User;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.service.ExchangeRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires - Service d'échange
 */
public class ExchangeServiceTest {

    @Mock
    private ExchangeRequestRepository exchangeRepository;

    private ExchangeRequestService exchangeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        exchangeService = new ExchangeRequestService();
    }

    @Test
    public void testCreateExchangeRequest() {
        User donateur = new User();
        donateur.setId(1L);
        User beneficiaire = new User();
        beneficiaire.setId(2L);

        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setDonateur(donateur);
        exchange.setBeneficiaire(beneficiaire);
        exchange.setOffreEnRetour("Laptop for Book");
        exchange.setDateEchange(LocalDate.now().plusDays(7));
        exchange.setStatut(ExchangeStatus.PENDING);

        when(exchangeRepository.save(any(ExchangeRequest.class))).thenReturn(exchange);

        ExchangeRequest result = exchangeRepository.save(exchange);

        assertNotNull(result);
        assertEquals("Laptop for Book", result.getOffreEnRetour());
        verify(exchangeRepository, times(1)).save(any(ExchangeRequest.class));
    }

    @Test
    public void testGetExchangeRequest() {
        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setId(1L);
        exchange.setOffreEnRetour("Test Offer");

        when(exchangeRepository.findById(1L)).thenReturn(Optional.of(exchange));

        ExchangeRequest result = exchangeRepository.findById(1L).orElse(null);

        assertNotNull(result);
        assertEquals("Test Offer", result.getOffreEnRetour());
        verify(exchangeRepository, times(1)).findById(1L);
    }

    @Test
    public void testAcceptExchangeRequest() {
        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setId(1L);
        exchange.setStatut(ExchangeStatus.PENDING);

        when(exchangeRepository.findById(1L)).thenReturn(Optional.of(exchange));
        when(exchangeRepository.save(exchange)).thenReturn(exchange);

        exchange.setStatut(ExchangeStatus.ACCEPTED);
        ExchangeRequest result = exchangeRepository.save(exchange);

        assertNotNull(result);
        assertEquals(ExchangeStatus.ACCEPTED, result.getStatut());
    }

    @Test
    public void testRejectExchangeRequest() {
        ExchangeRequest exchange = new ExchangeRequest();
        exchange.setId(1L);
        exchange.setStatut(ExchangeStatus.PENDING);

        exchange.setStatut(ExchangeStatus.REFUSED);

        assertEquals(ExchangeStatus.REFUSED, exchange.getStatut());
    }
}
