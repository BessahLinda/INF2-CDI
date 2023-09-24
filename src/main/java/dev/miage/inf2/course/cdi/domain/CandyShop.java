package dev.miage.inf2.course.cdi.domain;

import dev.miage.inf2.course.cdi.exception.OutOfStockException;
import dev.miage.inf2.course.cdi.interceptor.AgeAndWeightCheck;
import dev.miage.inf2.course.cdi.model.Candy;
import dev.miage.inf2.course.cdi.model.Customer;
import dev.miage.inf2.course.cdi.model.Receipt;
import dev.miage.inf2.course.cdi.service.CandyInventoryService;
import dev.miage.inf2.course.cdi.service.ReceiptTransmissionService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.Random;

@ApplicationScoped
public class CandyShop implements Shop<Candy> {

    @Inject
    Event<CandyCreatedEvent> event;

    @Inject
    @ForCandyStore
    protected CandyInventoryService<Candy> inventoryService;

    @Inject
    @ForCandyStore
    protected ReceiptTransmissionService<Candy> receiptTransmissionService;

    public CandyShop() {
    }

    public double countKilosOfCandy(String flavor) {
        return this.inventoryService.countKilosOfCandyInInventory(flavor);
    }


    @AgeAndWeightCheck
    public Candy sell(Customer customer, String flavor, double weightInKg) throws OutOfStockException {
        var soldCandy = this.inventoryService.takeFromInventoryByWeight(flavor, weightInKg);
        Receipt<Candy> receipt = new Receipt<Candy>(soldCandy, new Random().nextInt(0, 30), 0.055);
        receiptTransmissionService.sendReceipt(customer, receipt);
        return soldCandy;
    }



    @Override
    public void stock(Candy candy) {
        this.inventoryService.addToInventory(candy);
        event.fire(new CandyCreatedEvent(candy));
    }

    @Override
    public Collection<Candy> getAllItems() {
        return this.inventoryService.listAllItems();
    }
}
