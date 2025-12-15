package com.localexchange.service;

import com.localexchange.dto.MessageDTO;
import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.Message;
import com.localexchange.model.Notification;
import com.localexchange.model.NotificationType;
import com.localexchange.model.User;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.MessageRepository;
import com.localexchange.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Mock
    private MessageRepository mockMessageRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ExchangeRequestRepository mockExchangeRequestRepository;

    @Mock
    private NotificationService mockNotificationService;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessageSuccess() {
        // Arrange
        User expediteur = new User();
        expediteur.setId(1L);
        expediteur.setEmail("expediteur@mail.fr");
        expediteur.setNom("Alice");

        User destinataire = new User();
        destinataire.setId(2L);
        destinataire.setEmail("destinataire@mail.fr");
        destinataire.setNom("Bob");

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);
        exchangeRequest.setDonateur(expediteur);
        exchangeRequest.setBeneficiaire(destinataire);

        MessageDTO dto = new MessageDTO();
        dto.setContenu("Bonjour");
        dto.setExchangeRequestId(100L);

        when(mockUserRepository.findByEmail("expediteur@mail.fr")).thenReturn(Optional.of(expediteur));
        when(mockExchangeRequestRepository.findById(100L)).thenReturn(Optional.of(exchangeRequest));
        when(mockMessageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            msg.setId(999L);
            return msg;
        });
        when(mockNotificationService.createNotification(
                anyLong(), any(), anyString(), anyLong(), any(), any()
        )).thenReturn(new Notification()); // ou un objet Notification adapté


        // Act
        MessageDTO result = messageService.sendMessage(dto, "expediteur@mail.fr");

        // Assert
        assertNotNull(result);
        assertEquals("Bonjour", result.getContenu());
        assertEquals(expediteur.getId(), result.getExpediteurId());
        assertEquals(destinataire.getId(), result.getDestinataireId());
        verify(mockNotificationService, times(1)).createNotification(
                eq(destinataire.getId()),
                eq(NotificationType.NEW_MESSAGE),
                contains("Alice"),
                eq(exchangeRequest.getId()),
                isNull(),
                isNull()
        );
    }

    @Test
    public void testMarkAsReadSuccess() {
        // Arrange
        User destinataire = new User();
        destinataire.setId(2L);
        destinataire.setEmail("destinataire@mail.fr");
        destinataire.setNom("Bob");

        User expediteur = new User();
        expediteur.setId(1L);
        expediteur.setEmail("expediteur@mail.fr");
        expediteur.setNom("Alice");

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setId(100L);

        Message message = new Message();
        message.setId(500L);
        message.setContenu("Salut");
        message.setLu(false);
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);
        message.setExchangeRequest(exchangeRequest);

        when(mockMessageRepository.findById(500L)).thenReturn(Optional.of(message));
        when(mockUserRepository.findByEmail("destinataire@mail.fr")).thenReturn(Optional.of(destinataire));
        when(mockMessageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            msg.setLu(true);
            return msg;
        });

        // Act
        MessageDTO result = messageService.markAsRead(500L, "destinataire@mail.fr");

        // Assert
        assertNotNull(result);
        assertTrue(result.getLu());
        assertEquals("Salut", result.getContenu());
        assertEquals(destinataire.getId(), result.getDestinataireId());
    }
}
