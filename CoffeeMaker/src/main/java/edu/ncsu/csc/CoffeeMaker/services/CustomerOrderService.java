package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerOrderRepository;

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
public class CustomerOrderService extends Service<CustomerOrder, Long> {

    /**
     * OrderRepository, to be autowired in by Spring and provide CRUD operations
     * on Order model.
     */
    @Autowired
    private CustomerOrderRepository orderRepository;

    @Override
    protected JpaRepository<CustomerOrder, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find a order with the provided CustomerOrder ID
     *
     * @param name
     *            name identifier of the customerorder to find
     * @return found customerorder, null if none
     */
    public CustomerOrder findByName ( final String name ) {
        return orderRepository.findByName( name );
    }

}
