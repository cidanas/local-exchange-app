package com.localexchange.integration;

import com.localexchange.model.ExchangeRequest;
import com.localexchange.model.Message;
import com.localexchange.model.User;
import com.localexchange.repository.ExchangeRequestRepository;
import com.localexchange.repository.MessageRepository;
import com.localexchange.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration - Messagerie chat
 */
@SpringBootTest
@Transactional
@SuppressWarnings("null")
public class MessagingIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExchangeRequestRepository exchangeRepository;

    private User user1;
    private User user2;
    private ExchangeRequest exchange;

    @BeforeEach
    public void setup() {
        // Créer les utilisateurs
        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setPassword("hashedPassword1");
        user1.setNom("User 1");
        user1.setLocalisation("Paris");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setPassword("hashedPassword2");
        user2.setNom("User 2");
        user2.setLocalisation("Lyon");
        user2 = userRepository.save(user2);

        // Créer un échange
        exchange = new ExchangeRequest();
        exchange.setDonateur(user1);
        exchange.setBeneficiaire(user2);
        exchange.setOffreEnRetour("Book for Laptop");
        exchange.setDateEchange(java.time.LocalDate.now().plusDays(7));
        exchange = exchangeRepository.save(exchange);
    }

    @Test
    public void testSendMessage() {
        Message message = new Message();
        message.setContenu("Bonjour, intéressé par cet échange?");
        message.setExpediteur(user1);
        message.setDestinataire(user2);
        message.setExchangeRequest(exchange);

        Message saved = messageRepository.save(message);

        assertNotNull(saved.getId());
        assertEquals("Bonjour, intéressé par cet échange?", saved.getContenu());
        assertEquals(user1.getId(), saved.getExpediteur().getId());
    }

    @Test
    public void testMarkMessageAsRead() {
        Message message = new Message();
        message.setContenu("Test message");
        message.setExpediteur(user1);
        message.setDestinataire(user2);
        message.setExchangeRequest(exchange);
        message.setLu(false);

        Message saved = messageRepository.save(message);
        assertFalse(saved.getLu());

        saved.setLu(true);
        Message updated = messageRepository.save(saved);

        assertTrue(updated.getLu());
    }

    @Test
    public void testConversationFlow() {
        // Message 1: user1 -> user2
        Message msg1 = new Message();
        msg1.setContenu("Salut, tu es intéressé?");
        msg1.setExpediteur(user1);
        msg1.setDestinataire(user2);
        msg1.setExchangeRequest(exchange);
        msg1.setLu(false);
        messageRepository.save(msg1);

        // Message 2: user2 -> user1
        Message msg2 = new Message();
        msg2.setContenu("Oui, bien sûr!");
        msg2.setExpediteur(user2);
        msg2.setDestinataire(user1);
        msg2.setExchangeRequest(exchange);
        msg2.setLu(false);
        messageRepository.save(msg2);

        // Vérifier que les deux messages existent et qu'ils ont les bons contenus
        assertNotNull(msg1.getId());
        assertNotNull(msg2.getId());
        assertEquals("Salut, tu es intéressé?", msg1.getContenu());
        assertEquals("Oui, bien sûr!", msg2.getContenu());
    }
}
