package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

/**
 * Order for the coffee maker. Order is tied to the database using Hibernate
 * libraries. See OrderRepository and OrderService for the other two pieces
 * used for database support.
 *
 * @author cbdocke2
 *
 */
public class Order extends DomainObject {

    /** Order id */
    @Id
    @GeneratedValue
    private Long    id;

    /**
     * Represents the username identifier for a Order object, must be between 6
     * and 20 characters
     */
    @Length ( min = 6, max = 20 )
    private String  customerUsername;
    
    /**
     * Represents the recipe identifier for an Order object.
     */
    private Recipe  recipe;
    
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
     * @param customerUsername
     *            represents username of Order object
     * @param recipe
     *            represents recipe of Order object
     * @param isFulfilled
     *            boolean representing whether order has been fulfilled
     */
    public Order ( final String customerUsername, final Recipe recipe, final boolean isFulfilled ) {
        this();
        setCustomerUsername( customerUsername );
        setRecipe( recipe );
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

    public String getCustomerUsername () {
        return customerUsername;
    }

    public void setCustomerUsername ( final String customerUsername ) {
        this.customerUsername = customerUsername;
    }

    public Recipe getRecipe () {
        return recipe;
    }

    public void setRecipe ( final Recipe recipe ) {
        this.recipe = recipe;
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
    public String toString() {
        String orderString = customerUsername + ", " + recipe.getName() + ", "
                + isFulfilled;
        
        return orderString;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerUsername, recipe, isFulfilled);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        return Objects.equals(customerUsername, other.customerUsername) && 
                Objects.equals(recipe, other.recipe) && 
                Objects.equals( isFulfilled, other.isFulfilled );
    }

}
