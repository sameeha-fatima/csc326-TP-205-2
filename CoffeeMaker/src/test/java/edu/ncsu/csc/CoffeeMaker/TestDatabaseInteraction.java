package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests Database interaction of CoffeeMaker with Recipe
 *
 * @author Ellie Murphy
 *
 */
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class TestDatabaseInteraction {

	/** RecipeService object to be used for testing */
	@Autowired
	private RecipeService recipeService;

	/**
	 * Sets up the tests.
	 */

	@BeforeEach
	public void setup() {
		recipeService.deleteAll();
	}

	/**
	 * Tests the RecipeService class
	 */
	@Test
	@Transactional
	public void testRecipes() {

		final Recipe r = new Recipe();

		/* set fields here */
		r.setName("Mocha");
		r.setPrice(350);
		r.addIngredients(new Ingredient("Coffee", 2));
		r.addIngredients(new Ingredient("Sugar", 1));
		r.addIngredients(new Ingredient("Milk", 1));
		r.addIngredients(new Ingredient("Chocolate", 1));

		recipeService.save(r);

		final List<Recipe> dbRecipes = recipeService.findAll();

		assertEquals(1, dbRecipes.size());

		final Recipe dbRecipe = dbRecipes.get(0);

		assertEquals(r.getName(), dbRecipe.getName());
		/* Test all of final the fields! final You can also us assertAll. */
		assertEquals(r.getIngredients("Coffee"), dbRecipe.getIngredients("Coffee"));
		assertEquals(r.getIngredients("Sugar"), dbRecipe.getIngredients("Sugar"));
		assertEquals(r.getIngredients("Milk"), dbRecipe.getIngredients("Milk"));
		assertEquals(r.getIngredients("Chocolate"), dbRecipe.getIngredients("Chocolate"));
	}

	/**
	 * Tests the RecipeService class, specifically RecipeService.findByName()
	 */
	@Test
	@Transactional
	public void testRecipeByName() {

		final Recipe r = new Recipe();

		/* set fields here */
		r.setName("Mocha");
		r.setPrice(350);
		r.addIngredients(new Ingredient("Coffee", 2));
		r.addIngredients(new Ingredient("Sugar", 1));
		r.addIngredients(new Ingredient("Milk", 1));
		r.addIngredients(new Ingredient("Chocolate", 1));

		recipeService.save(r);

		assertEquals(r, recipeService.findByName("Mocha"));
	}

	/**
	 * Tests the Service.save() functionality
	 */
	@Test
	@Transactional
	public void testEditRecipe() {

		final Recipe r = new Recipe();

		/* set fields here */
		r.setName("Mocha");
		r.setPrice(350);
		r.addIngredients(new Ingredient("Coffee", 2));
		r.addIngredients(new Ingredient("Sugar", 1));
		r.addIngredients(new Ingredient("Milk", 1));
		r.addIngredients(new Ingredient("Chocolate", 1));
		recipeService.save(r);

		final List<Recipe> dbRecipes = recipeService.findAll();

		assertEquals(1, dbRecipes.size());

		final Recipe dbRecipe = recipeService.findByName("Mocha");

		/* Test all of final the fields! final You can also us assertAll. */
		assertEquals(r.getIngredients("Coffee"), dbRecipe.getIngredients("Coffee"));
		assertEquals(r.getIngredients("Sugar"), dbRecipe.getIngredients("Sugar"));
		assertEquals(r.getIngredients("Milk"), dbRecipe.getIngredients("Milk"));
		assertEquals(r.getIngredients("Chocolate"), dbRecipe.getIngredients("Chocolate"));

		r.setPrice(15);
		r.updateIngredient("Sugar", 12);
		recipeService.save(r);
		assertEquals(r.getPrice(), dbRecipe.getPrice());
		assertEquals(r.getIngredients("Sugar"), dbRecipe.getIngredients("Sugar"));
	}
}