package dev.miage.inf2.course.cdi.service;

import dev.miage.inf2.course.cdi.model.Candy;

public interface CandyInventoryService<C> extends InventoryService<Candy> {
    double countKilosOfCandyInInventory(String flavor);

    C takeFromInventoryByWeight(String flavor, double weightInKg);
}