
package edu.ncsu.csc.CoffeeMaker.generator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
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
public class GenerateRecipesSimple {

	/** RecipeService object for testing */
	@Autowired
	private RecipeService recipeService;

	/**
	 * Tests createRecipe()
	 */
	@Test
	public void testCreateRecipes() {

		recipeService.deleteAll();

		final Recipe r1 = new Recipe();
//		r1.setIngredient("Chocolate", 0);
		r1.addIngredients(new Ingredient("Coffee", 1));
//		r1.setMilk(0);
		r1.setName("Black Coffee");
		r1.setPrice(1);
//		r1.setSugar(0);

		final Recipe r2 = new Recipe();
//		r2.setChocolate(0);
		r2.addIngredients(new Ingredient("Coffee", 1));
		r2.addIngredients(new Ingredient("Milk", 1));
		r2.setName("Mocha");
		r2.setPrice(3);
		r2.addIngredients(new Ingredient("Sugar", 1));

		recipeService.save(r1);
		recipeService.save(r2);

		Assert.assertEquals("Creating two recipes should results in two recipes in the database", 2,
				recipeService.count());

	}

}