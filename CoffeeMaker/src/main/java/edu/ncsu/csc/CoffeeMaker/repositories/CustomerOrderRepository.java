package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;

/**
 * OrderRepository is used to provide CRUD operations for the CustomerOrder
 * model. Spring will generate appropriate code with JPA.
 *
 * @author cbdocke2
 *
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * Finds a CustomerOrder object with the provided name. Spring will generate
     * code to make this happen.
     *
     * @param name
     *            Name of the customerorder
     * @return Found CustomerOrder, null if none.
     */
    CustomerOrder findByName ( String name );

}
