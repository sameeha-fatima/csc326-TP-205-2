package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Tests Inventory class, see also Recipe and Ingredient
 *
 * @author Ellie Murphy
 *
 */
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class InventoryTest {

	/**
	 * InventoryService object for testing
	 */
	@Autowired
	private InventoryService inventoryService;

	/**
	 * Deletes all inventory from database before running tests and creates standard
	 * set of ingredients to replace inventory with
	 */
	@BeforeEach
	public void setup() {
		inventoryService.deleteAll();
		final Inventory ivt = inventoryService.getInventory();

		final Ingredient coffee = new Ingredient("Coffee", 500);
		final Ingredient milk = new Ingredient("Milk", 500);
		final Ingredient sugar = new Ingredient("Sugar", 500);
		final Ingredient chocolate = new Ingredient("Chocolate", 500);
		ivt.addIngredients(coffee);
		ivt.addIngredients(milk);
		ivt.addIngredients(sugar);
		ivt.addIngredients(chocolate);

	}

	/**
	 * tests consumeInventory()
	 */
	@Test
	@Transactional
	public void testConsumeInventory() {
		final Inventory i = inventoryService.getInventory();

//		final Ingredient coffee = new Ingredient("Coffee", 500);
//		final Ingredient milk = new Ingredient("Milk", 500);
//		final Ingredient sugar = new Ingredient("Sugar", 500);
//		final Ingredient chocolate = new Ingredient("Chocolate", 500);
//		i.addIngredients(coffee);
//		i.addIngredients(milk);
//		i.addIngredients(sugar);
//		i.addIngredients(chocolate);

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");
		// recipe.setChocolate(10);
		// recipe.setMilk(20);
		// recipe.setSugar(5);
		// recipe.setCoffee(1);
		final Ingredient coffee = new Ingredient("Coffee", 1);
		final Ingredient milk = new Ingredient("Milk", 20);
		final Ingredient sugar = new Ingredient("Sugar", 5);
		final Ingredient chocolate = new Ingredient("Chocolate", 10);
		recipe.addIngredient(coffee);
		recipe.addIngredient(milk);
		recipe.addIngredient(sugar);
		recipe.addIngredient(chocolate);

		recipe.setPrice(5);

		i.useIngredients(recipe);

		/*
		 * Make sure that all of the inventory fields are now properly updated
		 */

		Assertions.assertEquals(490, (int) i.getIngredientAmount(chocolate.getName()));
		Assertions.assertEquals(480, (int) i.getIngredientAmount(milk.getName()));
		Assertions.assertEquals(495, (int) i.getIngredientAmount(sugar.getName()));
		Assertions.assertEquals(499, (int) i.getIngredientAmount(coffee.getName()));
	}

	/**
	 * Tests addInventory()
	 */
	@Test
	@Transactional
	public void testAddInventory1() {
		Inventory ivt = inventoryService.getInventory();

		// ivt.addIngredients( "coffee", 5 );
		// ivt.addIngredients( "milk", 3 );
		// ivt.addIngredients( "sugar", 7 );
		// ivt.addIngredients( "chocolate", 2 );
		final Ingredient coffee = new Ingredient("Coffee", 5);
		final Ingredient milk = new Ingredient("Milk", 3);
		final Ingredient sugar = new Ingredient("Sugar", 7);
		final Ingredient chocolate = new Ingredient("Chocolate", 2);
		ivt.addIngredients(coffee);
		ivt.addIngredients(milk);
		ivt.addIngredients(sugar);
		ivt.addIngredients(chocolate);

		/* Save and retrieve again to update with DB */
		inventoryService.save(ivt);

		ivt = inventoryService.getInventory();

		Assertions.assertEquals(505, (int) ivt.getIngredientAmount("Coffee"),
				"Adding to the inventory should result in correctly-updated values for coffee");
		Assertions.assertEquals(503, (int) ivt.getIngredientAmount("Milk"),
				"Adding to the inventory should result in correctly-updated values for milk");
		Assertions.assertEquals(507, (int) ivt.getIngredientAmount("Sugar"),
				"Adding to the inventory should result in correctly-updated values sugar");
		Assertions.assertEquals(502, (int) ivt.getIngredientAmount("Chocolate"),
				"Adding to the inventory should result in correctly-updated values chocolate");

	}

	/**
	 * Tests addInventory()
	 */
	@Test
	@Transactional
	public void testAddInventory2() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			// ivt.addIngredients(-5, 3, 7, 2);
			ivt.addIngredients("coffee", -5);
			ivt.addIngredients("milk", 3);
			ivt.addIngredients("sugar", 7);
			ivt.addIngredients("chocolate", 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("coffee"),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("milk"),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("sugar"),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("chocolate"),
					"Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate");
		}
	}

	/**
	 * Tests addInventory()
	 */
	@Test
	@Transactional
	public void testAddInventory3() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			// ivt.addIngredients(5, -3, 7, 2);
			ivt.addIngredients("coffee", 5);
			ivt.addIngredients("milk", -3);
			ivt.addIngredients("sugar", 7);
			ivt.addIngredients("chocolate", 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(505, (int) ivt.getIngredientAmount("coffee"),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("milk"),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("sugar"),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("chocolate"),
					"Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate");

		}

	}

	/**
	 * Tests addInventory()
	 */
	@Test
	@Transactional
	public void testAddInventory4() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			// ivt.addIngredients(5, 3, -7, 2);
			ivt.addIngredients("coffee", 5);
			ivt.addIngredients("milk", 3);
			ivt.addIngredients("sugar", -7);
			ivt.addIngredients("chocolate", 2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(505, (int) ivt.getIngredientAmount("coffee"),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee");
			Assertions.assertEquals(503, (int) ivt.getIngredientAmount("milk"),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("sugar"),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("chocolate"),
					"Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate");

		}

	}

	/**
	 * Tests addInventory()
	 */
	@Test
	@Transactional
	public void testAddInventory5() {
		final Inventory ivt = inventoryService.getInventory();

		try {
			// ivt.addIngredients(5, 3, 7, -2);
			ivt.addIngredients("coffee", 5);
			ivt.addIngredients("milk", 3);
			ivt.addIngredients("sugar", 7);
			ivt.addIngredients("chocolate", -2);
		} catch (final IllegalArgumentException iae) {
			Assertions.assertEquals(505, (int) ivt.getIngredientAmount("coffee"),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee");
			Assertions.assertEquals(503, (int) ivt.getIngredientAmount("milk"),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk");
			Assertions.assertEquals(507, (int) ivt.getIngredientAmount("sugar"),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar");
			Assertions.assertEquals(500, (int) ivt.getIngredientAmount("chocolate"),
					"Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate");

		}

	}

	/**
	 * Tests checkInventory()
	 */
	@Test
	@Transactional
	public void testCheckInventory() {
		final Inventory ivt = inventoryService.getInventory();

//		final Ingredient coffee = new Ingredient("Coffee", 10);
//		final Ingredient milk = new Ingredient("Milk", 10);
//		final Ingredient sugar = new Ingredient("Sugar", 10);
//		final Ingredient chocolate = new Ingredient("Chocolate", 10);
//		ivt.addIngredients(coffee);
//		ivt.addIngredients(milk);
//		ivt.addIngredients(sugar);
//		ivt.addIngredients(chocolate);

		assertEquals(500, ivt.getIngredientAmount("Chocolate"));
		assertEquals(500, ivt.getIngredientAmount("Sugar"));
		assertEquals(500, ivt.getIngredientAmount("Coffee"));
		assertEquals(500, ivt.getIngredientAmount("Milk"));

		// Testing invalid values
//		final Exception e1 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("-10"));
//		assertEquals("Units of chocolate must be a positive integer", e1.getMessage());
//		final Exception e2 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Sugar"));
//		assertEquals("Units of sugar must be a positive integer", e2.getMessage());
//		final Exception e3 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Milk"));
//		assertEquals("Units of milk must be a positive integer", e3.getMessage());
//		final Exception e4 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Coffee"));
//		assertEquals("Units of coffee must be a positive integer", e4.getMessage());
	}

	/**
	 * Tests toString()
	 */
	@Test
	@Transactional
	public void testToString() {
		final Inventory ivt = inventoryService.getInventory();

		assertEquals("Coffee: 500\n" + "Milk: 500\n" + "Sugar: 500\n" + "Chocolate: 500\n", ivt.toString());
	}

	/**
	 * Tests enoughIngredients()
	 */
	@Test
	@Transactional
	public void testEnoughIngredients() {
		final Inventory ivt = inventoryService.getInventory();

		final Recipe r = new Recipe();
		r.setName("Mocha");
		final Ingredient coffee = new Ingredient("Coffee", 501);
		final Ingredient milk = new Ingredient("Milk", 501);
		final Ingredient sugar = new Ingredient("Sugar", 501);
		final Ingredient chocolate = new Ingredient("Chocolate", 501);
		r.addIngredient(coffee);
		r.addIngredient(milk);
		r.addIngredient(sugar);
		r.addIngredient(chocolate);
		// r.setCoffee(501);
		// r.setChocolate(501);
		// r.setSugar(501);
		// r.setMilk(501);

		// test branch when coffee is not enough
		assertFalse(ivt.enoughIngredients(r));

		// test branch when milk is not enough
		// r.setCoffee(5);
		r.getIngredients().get(3).setAmount(5);
		assertFalse(ivt.enoughIngredients(r));

		// branch when sugar final is not enough
		// r.setMilk(5);
		r.getIngredients().get(1).setAmount(5);
		assertFalse(ivt.enoughIngredients(r));

		// branch when chocolate is not enough
		// r.setSugar(5);
		r.getIngredients().get(2).setAmount(5);
		assertFalse(ivt.enoughIngredients(r));
	}

//	@Test
//	@Transactional
//	public void testCheckInventory2() {
//		final Inventory ivt = inventoryService.getInventory();
//
//		// Testing invalid values
//		final Exception e1 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("5.5"));
//		assertEquals("Units of chocolate must be a positive integer", e1.getMessage());
//		final Exception e2 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Sugar"));
//		assertEquals("Units of sugar must be a positive integer", e2.getMessage());
//		final Exception e3 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Milk"));
//		assertEquals("Units of milk must be a positive integer", e3.getMessage());
//		final Exception e4 = assertThrows(IllegalArgumentException.class, () -> ivt.getIngredientAmount("Coffee"));
//		assertEquals("Units of coffee must be a positive integer", e4.getMessage());
//	}

}
