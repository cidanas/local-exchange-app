package com.localexchange.model;

public enum NotificationType {
    NEW_REQUEST,        // Nouvelle demande d'échange reçue
    REQUEST_ACCEPTED,   // Votre demande a été acceptée
    REQUEST_REFUSED,    // Votre demande a été refusée
    NEW_MESSAGE,        // Nouveau message reçu
    REVIEW_RECEIVED     // Nouvel avis reçu
}