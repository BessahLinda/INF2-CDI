package dev.miage.inf2.course.cdi.service.impl;


import dev.miage.inf2.course.cdi.domain.ForCandyStore;

import dev.miage.inf2.course.cdi.model.Candy;
import dev.miage.inf2.course.cdi.model.Customer;
import dev.miage.inf2.course.cdi.model.Receipt;
import dev.miage.inf2.course.cdi.service.ReceiptTransmissionService;
import jakarta.enterprise.context.Dependent;

@Dependent
@ForCandyStore
public class StringReceiptTransmissionService implements ReceiptTransmissionService<Candy> {

    private static final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void sendReceipt(Customer customer, Receipt receipt) {
        stringBuilder.append("Merci d'avoir acheté " + receipt.item().toString() + " pour un montant de " + receipt.price()).append("\n");
    }
}
