package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
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
public class Order extends DomainObject {

    /** Order id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Order's Unique ID */
    @Id
    private String  orderId;

    /**
     * Represents the username identifier for a Order object, must be between 6
     * and 20 characters
     */
    @Length ( min = 6, max = 20 )
    private String  customerUsername;

    /**
     * Represents the recipe identifier for an Order object.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Recipe>  beverages;

    /**
     * Represents the isFulfilled identifier for an Order object.
     */
    private boolean isFulfilled;

    /**
     * Creates a default user for the CoffeeMaker system. Used by Hibernate
     */
    public Order () {
        super();
    }

    /**
     * All-argument constructor for Order
     *
     * @param orderId
     *            represents orderId of Order object
     * @param customerUsername
     *            represents username of Order object
     * @param recipe
     *            represents recipe of Order object
     * @param isFulfilled
     *            boolean representing whether order has been fulfilled
     */
    public Order ( final String orderId, final String customerUsername, final List<Recipe> beverages,
            final boolean isFulfilled ) {
        this();
        setOrderId( orderId );
        setCustomerUsername( customerUsername );
        setBeverages( beverages );
        setFulfilled( isFulfilled );
    }

    @Override
    public Long getId () {
        return id;
    }

    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId ( final String orderId ) {
        this.orderId = orderId;
    }

    public String getCustomerUsername () {
        return customerUsername;
    }

    public void setCustomerUsername ( final String customerUsername ) {
        this.customerUsername = customerUsername;
    }

    public List<Recipe> getBeverages () {
        return beverages;
    }

    public void setBeverages ( final List<Recipe> beverages ) {
        this.beverages = beverages;
    }

    public boolean isFulfilled () {
        return isFulfilled;
    }

    public void setFulfilled ( final boolean isFulfilled ) {
        this.isFulfilled = isFulfilled;
    }

    public void fulfillOrder () {
        setFulfilled( true );
    }

    /**
     * Returns the customerUsername and recipe name of the order.
     *
     * @return String
     */
    @Override
    public String toString () {
        String orderString = customerUsername + ", ";
        
        for(int i = 0; i < beverages.size(); i++) {
            orderString += beverages.get( i ).getName() + ", ";
        }
        
        orderString += isFulfilled;

        return orderString;
    }

    @Override
    public int hashCode () {
        return Objects.hash( customerUsername, beverages, isFulfilled );
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
        final Order other = (Order) obj;
        return Objects.equals( customerUsername, other.customerUsername ) && Objects.equals( beverages, other.beverages )
                && Objects.equals( isFulfilled, other.isFulfilled );
    }

}
