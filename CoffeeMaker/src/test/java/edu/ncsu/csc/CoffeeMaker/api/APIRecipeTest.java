package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests class for APIRecipe
 *
 * @author Ellie Murphy
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIRecipeTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	/** context for mvc object */
	@Autowired
	private WebApplicationContext context;

	/** Recipe service object */
	@Autowired
	private RecipeService service;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		service.deleteAll();
	}

	/**
	 * Ensures recipe has all ingredients
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void ensureRecipe() throws Exception {
		service.deleteAll();

		final Recipe r = new Recipe();
		r.addIngredient(new Ingredient("Coffee", 3));
		r.addIngredient(new Ingredient("Sugar", 8));
		r.addIngredient(new Ingredient("Milk", 4));
		r.addIngredient(new Ingredient("Chocolate", 5));

		r.setPrice(10);
		r.setName("Mocha");

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r)))
				.andExpect(status().isOk());

	}

	/**
	 * tests construction
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testRecipeAPI() throws Exception {

		service.deleteAll();

		final Recipe recipe = new Recipe();
		recipe.setName("Delicious Not-Coffee");
		recipe.addIngredient(new Ingredient("Coffee", 1));
		recipe.addIngredient(new Ingredient("Sugar", 5));
		recipe.addIngredient(new Ingredient("Milk", 20));
		recipe.addIngredient(new Ingredient("Chocolate", 10));

		recipe.setPrice(5);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(recipe)));

		assertEquals(1, (int) service.count());

	}

	/**
	 * tests add recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testAddRecipe2() throws Exception {

		/* Tests a recipe with a duplicate name to make sure it's rejected */

		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		final String name = "Coffee";
		final Recipe r1 = createRecipe(name, 50, 3, 1, 1, 0);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());

		final Recipe r2 = createRecipe(name, 50, 3, 1, 1, 0);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().is4xxClientError());

		assertEquals(1, service.findAll().size(), "There should only one recipe in the CoffeeMaker");
	}

	/**
	 * tests add recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testAddRecipe15() throws Exception {

		/* Tests to make sure that our cap of 3 recipes is enforced */

		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());
		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
//		service.save(r3);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r3)))
				.andExpect(status().isOk());

		assertEquals(3, service.count(), "Creating three recipes should result in three recipes in the database");

		final Recipe r4 = createRecipe("Hot Chocolate", 75, 1, 2, 1, 2);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r4)))
				.andExpect(status().isInsufficientStorage());

		assertEquals(3, service.count(), "Creating a fourth recipe should not get saved");
//		assertEquals(r3, service.findById(r3.getId()),
//				"Creating a fourth recipe should not replace the last recpie added.");
		assertEquals(null, service.findById(r4.getId()),
				"Creating a fourth recipe should not replace any of the recipes already created.");
	}

	private Recipe createRecipe(final String name, final Integer price, final Integer coffee, final Integer milk,
			final Integer sugar, final Integer chocolate) {
		final Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setPrice(price);
		recipe.addIngredient(new Ingredient("Coffee", coffee));
		recipe.addIngredient(new Ingredient("Sugar", sugar));
		recipe.addIngredient(new Ingredient("Milk", milk));
		recipe.addIngredient(new Ingredient("Chocolate", chocolate));

		return recipe;
	}

	/**
	 * tests delete recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testDeleteRecipe() throws Exception {
		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());
		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
//		service.save(r3);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r3)))
				.andExpect(status().isOk());

		assertEquals(3, service.count(), "Creating three recipes should result in three recipes in the database");

		final Recipe r4 = createRecipe("Hot Chocolate", 75, 1, 2, 1, 2);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r4)))
				.andExpect(status().isInsufficientStorage());

		assertEquals(3, service.count(), "Creating a fourth recipe should not get saved");
//		assertTrue(service.existsById(r2.getId()));

		mvc.perform(delete(String.format("/api/v1/recipes/%s", "Mocha")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertEquals(2, service.count());
//		assertFalse(service.existsById(r2.getId()));
	}

	/**
	 * tests add recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testAddAfterDelete() throws Exception {

		/* Tests to make sure that our cap of 3 recipes is enforced */

		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 0);
//		service.save(r1);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());
		final Recipe r2 = createRecipe("Mocha", 50, 3, 1, 1, 2);
//		service.save(r2);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());
		final Recipe r3 = createRecipe("Latte", 60, 3, 2, 2, 0);
//		service.save(r3);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r3)))
				.andExpect(status().isOk());

//		assertTrue(service.existsById(r2.getId()));
		assertEquals(3, service.count(), "Creating three recipes should result in three recipes in the database");
		mvc.perform(delete(String.format("/api/v1/recipes/%s", "Mocha")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertEquals(2, service.count());
//		assertFalse(service.existsById(r2.getId())); // check the correct
		// recipe was deleted

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());
		assertEquals(3, service.count(), "Creating three recipes should result in three recipes in the database");
		// assertTrue(service.existsById(r2.getId())); // check the recipe was
		// properly added back
		assertEquals(r2, service.findByName("Mocha"));
	}

	/**
	 * tests get recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testGetRecipe() throws Exception {
		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Recipe r1 = createRecipe("Coffee", 50, 3, 1, 1, 1);
//		service.save(r1);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());
		final Recipe r2 = createRecipe("Hot Cocoa", 50, 1, 3, 1, 3);
//		service.save(r2);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().isOk());

		assertEquals(2, service.count(), "Creating two recipes should result in two recipes in the database");
		final String recipe = mvc.perform(get(String.format("/api/v1/recipes/%s", "Coffee"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(recipe.contains("Coffee"));
		final String recipe2 = mvc.perform(get(String.format("/api/v1/recipes/%s", "Hot Cocoa"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(recipe2.contains("Hot Cocoa"));

		// no recipe found with this name
		mvc.perform(get(String.format("/api/v1/recipes/%s", "Mocha"))).andExpect(status().is4xxClientError());
	}

	/**
	 * tests edit recipe
	 *
	 * @throws Exception if something is wrong
	 */
	@Test
	@Transactional
	public void testEditRecipe() throws Exception {
		assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final List<Ingredient> i1 = new ArrayList<Ingredient>();
		i1.add(new Ingredient("coffee", 1));
		i1.add(new Ingredient("milk", 1));
		i1.add(new Ingredient("sugar", 1));
		i1.add(new Ingredient("chocolate", 1));
		final Recipe r1 = new Recipe(i1, 50);
		r1.setName("Coffee");
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r1)))
				.andExpect(status().isOk());

		Recipe r2 = new Recipe(i1, 25);
		r2.setName("Coffee");
		mvc.perform(put(String.format("/api/v1/recipes")).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r2))).andExpect(status().isOk());
		String checkRecipe = mvc.perform(get(String.format("/api/v1/recipes/%s", "Coffee"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(checkRecipe.contains("Coffee"));

		i1.remove(1); // remove milk ingredient from recipe
		r2 = new Recipe(i1, 25);
		r2.setName("Coffee");
		mvc.perform(put(String.format("/api/v1/recipes")).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r2))).andExpect(status().isOk());
		checkRecipe = mvc.perform(get(String.format("/api/v1/recipes/%s", "Coffee"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertFalse(checkRecipe.contains("milk"));

		i1.add(new Ingredient("tofu", 2)); // add tofu ingredient to recipe
		r2 = new Recipe(i1, 25);
		r2.setName("Coffee");
		mvc.perform(put(String.format("/api/v1/recipes")).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r2))).andExpect(status().isOk());
		checkRecipe = mvc.perform(get(String.format("/api/v1/recipes/%s", "Coffee"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(checkRecipe.contains("tofu"));

		r2.setName("Boba");
		mvc.perform(put(String.format("/api/v1/recipes")).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(r2))).andExpect(status().is4xxClientError());
	}
}