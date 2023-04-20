package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests class for Recipe
 *
 * @author ellvm
 *
 */
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class RecipeTest {

	/** Recipe service object */
	@Autowired
	private RecipeService service;

	/**
	 * To be completed before each test
	 */
	@BeforeEach
	public void setup() {
		service.deleteAll();
	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe() {

		final Recipe r1 = new Recipe();
		r1.setName("Black Coffee");
		r1.setPrice(1);
		r1.addIngredients(new Ingredient("Coffee", 1));
		assertEquals(1, r1.getIngredients().size());
		service.save(r1);

		final Recipe r2 = new Recipe();
		r2.setName("Mocha");
		r2.setPrice(1);
		r2.addIngredients(new Ingredient("Coffee", 1));
		r2.addIngredients(new Ingredient("Milk", 1));
		r2.addIngredients(new Ingredient("Sugar", 1));
		r2.addIngredients(new Ingredient("Chocolate", 1));

		assertEquals(4, r2.getIngredients().size());
		service.save(r2);

		final List<Recipe> recipes = service.findAll();
		Assertions.assertEquals(2, recipes.size(), "Creating two recipes should result in two recipes in the database");

		Assertions.assertEquals(r1, recipes.get(0), "The retrieved recipe should match the created one");
	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testNoRecipes() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = new Recipe();
		r1.setName("Tasty Drink");
		r1.setPrice(12);
		try {
			r1.addIngredients(new Ingredient("Coffee", -12));
			r1.addIngredients(new Ingredient("Milk", 0));
			r1.addIngredients(new Ingredient("Sugar", 0));
			r1.addIngredients(new Ingredient("Chocolate", 0));
			fail();
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals("Invalid Ingredient amount.", e.getMessage());
		}
//		r1.setCoffee(-12);
//		r1.setMilk(0);
//		r1.setSugar(0);
//		r1.setChocolate(0);

		final Recipe r2 = new Recipe();
		r2.setName("Mocha");
		r2.setPrice(1);
		r2.addIngredients(new Ingredient("Coffee", 1));
		r2.addIngredients(new Ingredient("Milk", 1));
		r2.addIngredients(new Ingredient("Sugar", 1));
		r2.addIngredients(new Ingredient("Chocolate", 1));

//		final List<Recipe> recipes = List.of(r1, r2);
//
//		try {
//			service.saveAll(recipes);
//			Assertions.assertEquals(0, service.count(),
//					"Trying to save a collection of elements where one is invalid should result in neither getting saved");
//		} catch (final Exception e) {
//			Assertions.assertTrue(e instanceof ConstraintViolationException);
//		}

	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe1() {

		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		final Recipe r1 = createRecipe(name, 50, 3, 1, 1, 0);

		service.save(r1);

		Assertions.assertEquals(1, service.findAll().size(), "There should only one recipe in the CoffeeMaker");
		Assertions.assertNotNull(service.findByName(name));

	}

	/* Test2 is done via the API for different validation */

	/**
	 * Tests edit recipe
	 */
	@Test
	@Transactional
	public void testEditRecipe() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";

		final List<Ingredient> i1 = new ArrayList<Ingredient>(); // original list of ingredients
		i1.add(new Ingredient("Chocolate", 1));
		i1.add(new Ingredient("Milk", 1));
		i1.add(new Ingredient("Sugar", 1));
		i1.add(new Ingredient("Beans", 1));
		final Recipe recipe = new Recipe(i1, 15); // this will be our original recipe
		recipe.setName(name);

		// test updating price of recipe
//		recipe.editRecipe(20, i1);
//		assertEquals(20, recipe.getPrice()); // ensure price changed
//		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients didn't change
//			assertEquals(i1.get(i), recipe.getIngredients().get(i));
//		}

		recipe.editRecipe(new Recipe(i1, 20));
		assertEquals(20, recipe.getPrice()); // ensure price changed
		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients didn't change
			assertEquals(i1.get(i), recipe.getIngredients().get(i));
		}

		// test updating one of the units of the ingredient list
//		final List<Ingredient> i2 = new ArrayList<Ingredient>();
//		i2.add(new Ingredient("Chocolate", 2));
//		i2.add(new Ingredient("Milk", 1));
//		i2.add(new Ingredient("Sugar", 1));
//		i2.add(new Ingredient("Beans", 1));
//		recipe.editRecipe(20, i2);
//		assertEquals(20, recipe.getPrice()); // ensure price did not change
//		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
//			assertEquals(i2.get(i), recipe.getIngredients().get(i));
//		}

		final List<Ingredient> i2 = new ArrayList<Ingredient>();
		i2.add(new Ingredient("Chocolate", 2));
		i2.add(new Ingredient("Milk", 1));
		i2.add(new Ingredient("Sugar", 1));
		i2.add(new Ingredient("Beans", 1));
		recipe.editRecipe(new Recipe(i2, 20));
		assertEquals(20, recipe.getPrice()); // ensure price did not change
		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
			assertEquals(i2.get(i), recipe.getIngredients().get(i));
		}

		// test with an additional ingredient
//		i2.add(new Ingredient("Tofu", 1));
//		recipe.editRecipe(20, i2);
//		assertEquals(20, recipe.getPrice()); // ensure price did not change
//		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
//			assertEquals(i2.get(i), recipe.getIngredients().get(i));
//		}
		i2.add(new Ingredient("Tofu", 1));
		recipe.editRecipe(new Recipe(i2, 20));
		assertEquals(20, recipe.getPrice()); // ensure price did not change
		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
			assertEquals(i2.get(i), recipe.getIngredients().get(i));
		}

		// test after removing an ingredient
//		i2.remove(0);
//		recipe.editRecipe(20, i2);
//		assertEquals(20, recipe.getPrice()); // ensure price did not change
//		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
//			assertEquals(i2.get(i), recipe.getIngredients().get(i));
//		}
		i2.remove(0);
		recipe.editRecipe(new Recipe(i2, 20));
		assertEquals(20, recipe.getPrice()); // ensure price did not change
		for (int i = 0; i < recipe.getIngredients().size(); i++) { // ensure ingredients changed
			assertEquals(i2.get(i), recipe.getIngredients().get(i));
		}

	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe4() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		try {
			createRecipe(name, 50, -3, 1, 1, 2);
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals("Invalid Ingredient amount.", e.getMessage());
		}

//		try {
//			service.save(r1);
//
//			Assertions.assertNull(service.findByName(name),
//					"A recipe was able to be created with a negative amount of coffee");
//		} catch (final ConstraintViolationException cvee) {
//			// expected
//		}

	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe5() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		try {
			createRecipe(name, 50, 3, -1, 1, 2);
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals("Invalid Ingredient amount.", e.getMessage());
		}
	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe6() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		try {
			createRecipe(name, 50, 3, 1, -1, 2);
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals("Invalid Ingredient amount.", e.getMessage());
		}

	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe7() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		try {
			createRecipe(name, 50, 3, 1, 1, -2);
		} catch (final IllegalArgumentException e) {
			Assertions.assertEquals("Invalid Ingredient amount.", e.getMessage());
		}
	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe13() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
		service.save(r2);

		Assertions.assertEquals(2, service.count(),
				"Creating two recipes should result in two recipes in the database");

	}

	/**
	 * Tests add recipe
	 */
	@Test
	@Transactional
	public void testAddRecipe14() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
		service.save(r2);
		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
		service.save(r3);

		Assertions.assertEquals(3, service.count(),
				"Creating three recipes should result in three recipes in the database");

	}

	/**
	 * Tests delete recipe
	 */
	@Test
	@Transactional
	public void testDeleteRecipe1() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);

		Assertions.assertEquals(1, service.count(), "There should be one recipe in the database");

		service.delete(r1);
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
	}

	/**
	 * Tests delete recipe
	 */
	@Test
	@Transactional
	public void testDeleteRecipe2() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
		service.save(r2);
		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
		service.save(r3);

		Assertions.assertEquals(3, service.count(), "There should be three recipes in the database");

		service.deleteAll();

		Assertions.assertEquals(0, service.count(), "`service.deleteAll()` should remove everything");

	}

	/**
	 * Tests edit recipe
	 */
	@Test
	@Transactional
	public void testEditRecipe1() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);

		r1.setPrice(70);

		service.save(r1);

		final Recipe retrieved = service.findByName("Coffee");

		Assertions.assertEquals(70, (int) retrieved.getPrice());
		Assertions.assertEquals(3, (int) retrieved.getIngredients("Coffee"));
		Assertions.assertEquals(1, (int) retrieved.getIngredients("Milk"));
		Assertions.assertEquals(1, (int) retrieved.getIngredients("Sugar"));
		Assertions.assertEquals(0, (int) retrieved.getIngredients("Chocolate"));

		Assertions.assertEquals(1, service.count(), "Editing a recipe shouldn't duplicate it");

	}

	private Recipe createRecipe(final String name, final Integer price, final Integer coffee, final Integer milk,
			final Integer sugar, final Integer chocolate) {
		final Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setPrice(price);
		recipe.addIngredients(new Ingredient("Coffee", coffee));
		recipe.addIngredients(new Ingredient("Milk", milk));
		recipe.addIngredients(new Ingredient("Sugar", sugar));
		recipe.addIngredients(new Ingredient("Chocolate", chocolate));

		return recipe;
	}

	/**
	 * Tests update recipe
	 */
	@Test
	@Transactional
	public void testUpdateRecipe() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);
		assertFalse(r1.checkRecipe());
		final Recipe r2 = createRecipe("Coffee2", 45, 5, 6, 7, 8);
		service.save(r2);

		Assertions.assertEquals("Coffee", r1.getName());
		assertEquals(50, r1.getPrice());
		assertEquals(3, r1.getIngredients("Coffee"));
		assertEquals(1, r1.getIngredients("Milk"));
		assertEquals(1, r1.getIngredients("Sugar"));
		assertEquals(0, r1.getIngredients("Chocolate"));

		r1.updateRecipe(r2);

		Assertions.assertEquals("Coffee", r1.getName());
		assertEquals(5, r1.getIngredients("Coffee"));
		assertEquals(6, r1.getIngredients("Milk"));
		assertEquals(7, r1.getIngredients("Sugar"));
		assertEquals(8, r1.getIngredients("Chocolate"));
		assertEquals(45, r1.getPrice());
	}

	/**
	 * Tests equals with recipe
	 */
	@Test
	@Transactional
	public void testEquals() {
		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r1);
		final Recipe r2 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		service.save(r2);
		final Recipe r3 = createRecipe("Coffee2", 45, 5, 6, 7, 8);
		service.save(r3);
		final Recipe r4 = createRecipe("Coffee", 49, 3, 1, 1, 0);
		service.save(r4);
		final Recipe r5 = createRecipe("Coffee2", 50, 3, 1, 1, 0);
		service.save(r5);
		final Recipe r6 = createRecipe("Coffee", 50, 2, 1, 1, 0);
		service.save(r6);
		final Recipe r7 = createRecipe("Coffee", 50, 3, 2, 1, 0);
		service.save(r7);
		final Recipe r8 = createRecipe("Coffee", 50, 3, 1, 2, 0);
		service.save(r8);
		final Recipe r9 = createRecipe("Coffee", 50, 3, 1, 1, 2);
		service.save(r9);
//		final Recipe r10 = createRecipe(null, null, null, null, null, null);
//		service.save(r10);
		// compare two equal objects
		assertTrue(r1.equals(r2));
		// two different objects
		assertFalse(r1.equals(r3));
		// different name only
		assertFalse(r1.equals(r5));
		// different price only
		assertTrue(r1.equals(r4));
		// different coffee only
		assertTrue(r1.equals(r6));
		// different milk only
		assertTrue(r1.equals(r7));
		// different sugar only
		assertTrue(r1.equals(r8));
		// different chocolate only
		assertTrue(r1.equals(r9));
		// if other object is null
//		assertFalse(r1.equals(r10));
	}

	/**
	 * Tests toString with recipe
	 */
	@Test
	@Transactional
	public void testToString() {
		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
		r1.updateIngredient("Chocolate", 10);
		r1.updateIngredient("Coffee", 10);
		r1.updateIngredient("Milk", 10);
		r1.setName("Mocha");
		r1.setPrice(5);
		r1.updateIngredient("sugar", 10);
		Assertions.assertEquals(
				"Mocha: [Ingredient [id=null, name=Coffee, amount=10], Ingredient [id=null, name=Milk, amount=10], Ingredient [id=null, name=Sugar, amount=10]]",
				r1.toString());
	}
}
