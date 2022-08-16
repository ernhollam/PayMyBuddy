package com.paymybuddy.paymybuddy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(TransactionService.class)
class TransactionServiceTest {

    // TODO check de la somme négative
    // TODO check de la somme égale à 0
    // TODO check utilisateur n'a pas le solde suffisant
    // TODO le bénéficiaire est bien dans la liste des connexions de l'émetteur
    // TODO l'émetteur est bien débité (solde)
    // TODO le bénéficiaire est bien crédité (solde)
    // TODO la transaction est bien ajoutée dans la liste des transactions des deux utilisateurs concernés
    @Test
    void createTransaction() {
    }
}