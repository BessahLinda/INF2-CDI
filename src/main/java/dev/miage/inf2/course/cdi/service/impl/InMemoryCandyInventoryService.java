package dev.miage.inf2.course.cdi.service.impl;

import dev.miage.inf2.course.cdi.domain.ForCandyStore;
import dev.miage.inf2.course.cdi.exception.OutOfStockException;
import dev.miage.inf2.course.cdi.model.Candy;
import dev.miage.inf2.course.cdi.service.CandyInventoryService;
import jakarta.enterprise.context.Dependent;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.concurrent.LinkedBlockingDeque;

@Dependent
@ForCandyStore
public class InMemoryCandyInventoryService implements CandyInventoryService<Candy> {
    ConcurrentMap<String, BlockingDeque<Candy>> inventory = new ConcurrentHashMap<>();

    @Override
    public void addToInventory(Candy candyToAdd) {
        synchronized (candyToAdd.getFlavor()) {
            if (inventory.containsKey(candyToAdd.getFlavor())) {
                Candy existingCandy = inventory.get(candyToAdd.getFlavor()).peek();
                if (existingCandy != null) {
                    // Augmenter le poids du bonbon existant
                    existingCandy.setWeight(existingCandy.getWeight() + candyToAdd.getWeight());
                } else {
                    inventory.get(candyToAdd.getFlavor()).offer(candyToAdd);
                }
            } else {
                inventory.put(candyToAdd.getFlavor(), new LinkedBlockingDeque<>(List.of(candyToAdd)));
            }
        }
    }

    @Override
    public Candy takeFromInventory() {
        try {
            var candy = inventory.values().stream().filter(v -> v.size() > 0).findAny().orElseThrow().poll();
            if (candy == null) {
                throw new NoSuchElementException();
            }
            return candy;
        } catch (NoSuchElementException e) {
            throw new OutOfStockException("Désolé, nous n'avons plus de bonbons pour vous.");
        }
    }

    @Override
    public long countItemsInInventory() {
        return inventory.entrySet().stream().mapToInt(e -> e.getValue().size()).sum();
    }

    @Override
    public Collection<Candy> listAllItems() {
        return this.inventory.values().stream().flatMap(c -> c.stream()).collect(Collectors.toSet());
    }

    public void deleteCandyByFlavor(String flavor) {
        this.inventory.remove(flavor);
    }


    @Override
    public double countKilosOfCandyInInventory(String flavor) {
        if (!inventory.containsKey(flavor)) {
            return 0.0;  // Si la saveur n'est pas dans l'inventaire, retourner 0.
        }

        return inventory.get(flavor).stream()
                .mapToDouble(Candy::getWeight)  // Utiliser la méthode getWeight() pour obtenir le poids de chaque bonbon.
                .sum();  // Sommer tous les poids pour obtenir le poids total.
    }

    public boolean needsRestocking(String flavor) {
        return countKilosOfCandyInInventory(flavor) < 0.050;
    }

    public Candy takeFromInventoryByWeight(String flavor, double weightInKg) {
        if (!inventory.containsKey(flavor) || countKilosOfCandyInInventory(flavor) < weightInKg) {
            throw new OutOfStockException("Désolé, nous n'avons pas suffisamment de bonbons de cette saveur pour votre demande.");
        }

        double totalWeightRemoved = 0;
        while(totalWeightRemoved < weightInKg) {
            Candy candy = inventory.get(flavor).poll();
            if (candy == null) {
                throw new OutOfStockException("Désolé, une erreur s'est produite lors de la récupération des bonbons.");
            }
            totalWeightRemoved += candy.getWeight();
        }

        return new Candy(flavor, weightInKg);
    }


}

