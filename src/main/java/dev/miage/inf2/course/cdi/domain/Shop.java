package dev.miage.inf2.course.cdi.domain;

import dev.miage.inf2.course.cdi.exception.OutOfStockException;
import dev.miage.inf2.course.cdi.model.Customer;

import java.util.Collection;

/**
 * An interface representing a Shop that sells and stock things
 *
 * @param <T> the type of the things handled by the shop
 */
public interface Shop<T> {

    /**
     * Return the thing to be sold
     *
     * @param customer the customer bying the book
     * @return an instance of the thing to be sold
     * @throws OutOfStockException if no stock is available
     */
    T sell(Customer customer, String flavor, double weight) throws OutOfStockException;

    /**
     * Add an instance of thing in the class inventory, to be sold later
     *
     * @param t the instance to be sold later
     */

    void stock(T t);

    Collection<T> getAllItems();
}
