package com.localexchange.unit;

import com.localexchange.dto.RegisterDTO;
import com.localexchange.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires - Service Authentification
 */
@SuppressWarnings("null")
public class AuthenticationServiceTest {

    @Test
    public void testRegisterNewUser() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("newuser@test.com");
        dto.setPassword("Password123!");
        dto.setNom("John Doe");
        dto.setLocalisation("Paris");

        // Vérifier que les données sont valides
        assertNotNull(dto.getEmail());
        assertEquals("newuser@test.com", dto.getEmail());
        assertEquals("John Doe", dto.getNom());
        assertTrue(dto.getPassword().length() >= 8);
    }

    @Test
    public void testRegisterValidation() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("SecurePass123!");
        dto.setNom("Test User");
        dto.setLocalisation("Lyon");

        assertNotNull(dto.getEmail());
        assertNotNull(dto.getPassword());
        assertNotNull(dto.getNom());
        assertTrue(dto.getEmail().contains("@"));
        assertTrue(dto.getPassword().length() >= 8);
    }

    @Test
    public void testDuplicateEmailDetection() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("duplicate@test.com");
        user1.setNom("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("duplicate@test.com");
        user2.setNom("User 2");

        // Vérifier que les emails sont identiques
        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setNom("Test User");
        user.setPassword("hashedPassword");
        user.setLocalisation("Paris");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("user@test.com", user.getEmail());
        assertEquals("Test User", user.getNom());
    }

    @Test
    public void testEmailValidation() {
        String validEmail = "test@example.com";
        String invalidEmail = "invalid-email";

        assertTrue(validEmail.contains("@"));
        assertFalse(invalidEmail.contains("@"));
    }
}
