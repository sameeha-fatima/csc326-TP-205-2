package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * The OrderService is used to handle CRUD operations on the Order model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Order by name.
 *
 * @author cbdocke2
 *
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    /**
     * OrderRepository, to be autowired in by Spring and provide CRUD operations
     * on Order model.
     */
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find a order with the provided name
     *
     * @param name
     *            Name of the order to find
     * @return found order, null if none
     */
    public Order findByName ( final String name ) {
        return orderRepository.findByName( name );
    }

}
