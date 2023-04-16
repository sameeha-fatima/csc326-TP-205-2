package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;

/**
 * Order for the coffee maker. Order is tied to the database using Hibernate
 * libraries. See OrderRepository and OrderService for the other two pieces used
 * for database support.
 *
 * @author cbdocke2
 *
 */
@Entity
public class CustomerOrder extends DomainObject {

    /** Order id */
    @Id
    @GeneratedValue
    private Long               id;

    /** Order name */
    private String             name;

    /**
     * Represents the username identifier for a Order object, must be between 6
     * and 20 characters
     */
    @Length ( min = 6, max = 20 )
    private String             customer;

    /**
     * Represents the recipe identifier for an Order object.
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Recipe> beverages;

    /**
     * Represents the isFulfilled identifier for an Order object.
     */
    private boolean            isFulfilled;

    /**
     * Creates a default user for the CoffeeMaker system. Used by Hibernate
     */
    public CustomerOrder () {
        this.name = "";
        beverages = new ArrayList<Recipe>();
    }

    /**
     * All-argument constructor for Order
     *
     * @param name
     *            represents orderId of Order object
     * @param customer
     *            represents username of Order object
     * @param beverages
     *            represents the list of recipe objects
     */
    public CustomerOrder ( final String name, final String customer, final List<Recipe> beverages ) {
        this();
        for ( final Recipe r : beverages ) {
            this.beverages.add( r );
        }
        // setName( name );
        setCustomerUsername( customer );
        isFulfilled = false;
    }

    @Override
    public Long getId () {
        return id;
    }

    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns order ID of order
     *
     * @return order ID of order
     */
    public String getName () {
        return name;
    }

    /**
     * Set name of order
     *
     * @param name
     *            order ID of order
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns customer's username associated with the order
     *
     * @return customer's username associated with the order
     */
    public String getCustomerUsername () {
        return customer;
    }

    /**
     * Sets customer's username associated with the order
     *
     * @param customerUsername
     *            customer's username associated with the order
     */
    public void setCustomerUsername ( final String customerUsername ) {
        this.customer = customerUsername;
    }

    /**
     * Returns a list of beverages in the order
     *
     * @return a list of beverages in the order
     */
    public List<Recipe> getBeverages () {
        return beverages;
    }

    /**
     * Returns whether the order is fulfilled or not
     *
     * @return whether the order is fulfilled or not
     */
    public boolean isFulfilled () {
        return isFulfilled;
    }

    /**
     * Sets the order to be fulfilled
     *
     * @param inv
     *            Inventory to be looked at
     */
    public void fulfillOrder ( final Inventory inv ) {
        boolean checkClear = true;
        for ( int i = 0; i < beverages.size(); i++ ) {
            if ( !inv.enoughIngredients( beverages.get( i ) ) ) {
                checkClear = false;
            }
            else {
                inv.useIngredients( beverages.get( i ) );
            }
        }
        if ( checkClear ) {
            isFulfilled = true;
        }
    }

    /**
     * Returns the customerUsername and recipe name of the order.
     *
     * @return String
     */
    @Override
    public String toString () {
        String orderString = customer + ", ";

        for ( int i = 0; i < beverages.size(); i++ ) {
            orderString += beverages.get( i ).getName() + ", ";
        }

        orderString += isFulfilled;

        return orderString;
    }

    @Override
    public int hashCode () {
        return Objects.hash( customer, beverages, isFulfilled );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final CustomerOrder other = (CustomerOrder) obj;
        return Objects.equals( customer, other.customer ) && Objects.equals( beverages, other.beverages )
                && Objects.equals( isFulfilled, other.isFulfilled );
    }

}
