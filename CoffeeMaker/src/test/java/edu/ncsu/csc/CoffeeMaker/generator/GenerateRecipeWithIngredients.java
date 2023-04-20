package edu.ncsu.csc.CoffeeMaker.generator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests Recipe with arbitrary ingredients and database interactions
 *
 * @author Ellie Murphy
 *
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateRecipeWithIngredients {

	/** RecipeService object for testing */
	@Autowired
	private RecipeService recipeService;

	/** Deletes all recipe objects from database before running test */
	@Before
	public void setup() {
		recipeService.deleteAll();
	}

	/**
	 * Tests createRecipe()
	 */
	@Test
	public void createRecipe() {
		final Recipe r1 = new Recipe();
		r1.setName("Delicious Coffee");

		r1.setPrice(50);

		r1.addIngredients(new Ingredient("Coffee", 10));
		r1.addIngredients(new Ingredient("Pumpkin Spice", 3));
		r1.addIngredients(new Ingredient("Milk", 2));

		recipeService.save(r1);

		printRecipes();
	}

	private void printRecipes() {
		for (final DomainObject r : recipeService.findAll()) {
			System.out.println(r);
		}
	}

}
