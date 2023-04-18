package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * Tests the functionalities of Order
 *
 * @author Sameeha Fatima
 *
 */
public class CustomerOrderTest {
    /**
     * Tests Order name
     */
    @Test
    @Transactional
    public void testName () {
        final CustomerOrder o = new CustomerOrder();
        o.setName( "1" );

        assertEquals( "1", o.getName() );
    }

    /**
     * Tests Order's customer username
     */
    @Test
    @Transactional
    public void testCustomerName () {
        final CustomerOrder o = new CustomerOrder();
        o.setCustomerUsername( "sfatima" );

        assertEquals( "sfatima", o.getCustomerUsername() );
    }

    /**
     * Tests Order's list of beverages
     */
    @Test
    @Transactional
    public void testBeverages () {
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Oat Milk", 2 ) );
        ingredients.add( new Ingredient( "Matcha Powder", 3 ) );

        recipes.add( new Recipe( ingredients, 3 ) );
        final CustomerOrder o = new CustomerOrder( "1", "sfatima3", recipes );

        assertEquals( recipes.get( 0 ), o.getBeverages().get( 0 ) );
    }

    /**
     * Tests the order's fulfill order option
     */
    @Test
    @Transactional
    public void testFulfill () {
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Oat Milk", 2 ) );
        ingredients.add( new Ingredient( "Matcha Powder", 3 ) );

        recipes.add( new Recipe( ingredients, 3 ) );
        final CustomerOrder o = new CustomerOrder( "1", "sfatima3", recipes );

        assertEquals( recipes.get( 0 ), o.getBeverages().get( 0 ) );
        assertFalse( o.isFulfilled() );

        final List<Ingredient> ingredientsForInv = new ArrayList<Ingredient>();
        final Ingredient ing1 = new Ingredient( "Oat Milk", 3 );
        final Ingredient ing2 = new Ingredient( "Matcha Powder", 6 );
        ingredientsForInv.add( ing1 );
        ingredientsForInv.add( ing2 );
        final Inventory i = new Inventory( ingredientsForInv );

        o.fulfillOrder( i );
        assertTrue( o.isFulfilled() );
        assertEquals( (Integer) 1, i.getIngredientAmount( "Oat Milk" ) );
        assertEquals( (Integer) 3, i.getIngredientAmount( "Matcha Powder" ) );

        // invalid amount of ingredients

        final CustomerOrder o2 = new CustomerOrder( "2", "sfatima3", recipes );
        assertFalse( o2.isFulfilled() );
        o2.fulfillOrder( i );
        assertFalse( o2.isFulfilled() );
        assertEquals( (Integer) 1, i.getIngredientAmount( "Oat Milk" ) );
        assertEquals( (Integer) 3, i.getIngredientAmount( "Matcha Powder" ) );
    }

    /**
     * Tests the order's toString method
     */
    @Test
    @Transactional
    public void testToString () {
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( new Ingredient( "Oat Milk", 2 ) );
        ingredients.add( new Ingredient( "Matcha Powder", 3 ) );

        recipes.add( new Recipe( ingredients, 3 ) );
        final CustomerOrder o = new CustomerOrder( "1", "sfatima3", recipes );

        final String answer = "sfatima3, , false";
        assertEquals( answer, o.toString() );
    }
}
