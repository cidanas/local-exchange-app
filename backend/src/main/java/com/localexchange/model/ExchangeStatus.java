package com.localexchange.model;

public enum ExchangeStatus {
    PENDING,      // En attente de réponse
    ACCEPTED,     // Accepté par le donateur
    REFUSED,      // Refusé par le donateur
    COMPLETED,    // Échange terminé
    CANCELLED     // Annulé
}