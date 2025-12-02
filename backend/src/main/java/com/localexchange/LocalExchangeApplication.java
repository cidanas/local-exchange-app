package com.localexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale LocalExchange
 * Plateforme d'échange local de biens et compétences
 */
@SpringBootApplication
public class LocalExchangeApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LocalExchangeApplication.class, args);
        System.out.println("\n✅ LocalExchange Application démarrée avec succès!");
        System.out.println("📍 API disponible sur : http://localhost:8080");
        System.out.println("📖 Documentation : http://localhost:8080/api/docs\n");
    }
}