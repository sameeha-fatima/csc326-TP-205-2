package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests Recipe and Inventory database interactions
 *
 * @author Ellie Murphy
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	/** context for mvc object */
	@Autowired
	private WebApplicationContext context;

	/** RecipeService object for testing */
	@Autowired
	private RecipeService service;

	/** InventoryService object for testing */
	@Autowired
	private InventoryService iService;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		service.deleteAll();
		iService.deleteAll();
	}

	/**
	 * Tests recipe and inventory interactions
	 *
	 * @throws Exception if exception is thrown during testing
	 */
	@Test
	@Transactional
	public void testAPI() throws Exception {
		String recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		// Figure out if the recipe we want is present
		if (!recipe.contains("Mocha")) {
			// create a new Mocha recipe
			final Recipe r = new Recipe();
			r.setName("Mocha");
			r.setPrice(10);
			r.addIngredient(new Ingredient("Coffee", 2));
			r.addIngredient(new Ingredient("Sugar", 1));
			r.addIngredient(new Ingredient("Milk", 3));
			r.addIngredient(new Ingredient("Chocolate", 2));

			mvc.perform(
					post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r)))
					.andExpect(status().isOk());
		}

		recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		assertTrue(recipe.contains("Mocha"));

		final Ingredient coffee = new Ingredient("coffee", 50);
		final Ingredient milk = new Ingredient("milk", 50);
		final Ingredient sugar = new Ingredient("sugar", 50);
		final Ingredient chocolate = new Ingredient("chocolate", 50);
		final List<Ingredient> list = new ArrayList<Ingredient>();
		list.add(chocolate);
		list.add(coffee);
		list.add(sugar);
		list.add(milk);
		final Inventory i = new Inventory(list);

		// update the inventory with +50 for all items
		mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i)))
				.andExpect(status().isOk());

		// Make coffee
		mvc.perform(post(String.format("/api/v1/makecoffee/%s", "Mocha")).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(100))).andExpect(status().isOk()).andDo(print());

	}

	/**
	 * Tests getInventory()
	 *
	 * @throws Exception if exception is thrown during testing
	 */
	@Test
	@Transactional
	public void testGetInventory() throws Exception {

		// Test APIInventoryController getInventory method
		final Ingredient coffee = new Ingredient("coffee", 10);
		final Ingredient milk = new Ingredient("milk", 10);
		final Ingredient sugar = new Ingredient("sugar", 10);
		final Ingredient chocolate = new Ingredient("chocolate", 10);
		final List<Ingredient> list = new ArrayList<Ingredient>();
		list.add(chocolate);
		list.add(coffee);
		list.add(sugar);
		list.add(milk);
		final Inventory i = new Inventory(list);
		iService.save(i);

		final String ivt = mvc.perform(get("/api/v1/inventory")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertTrue(ivt.contains("\"name\":\"chocolate\",\"amount\":10"));
		assertTrue(ivt.contains("\"name\":\"coffee\",\"amount\":10"));
		assertTrue(ivt.contains("\"name\":\"sugar\",\"amount\":10"));
		assertTrue(ivt.contains("\"name\":\"milk\",\"amount\":10"));
	}
}
