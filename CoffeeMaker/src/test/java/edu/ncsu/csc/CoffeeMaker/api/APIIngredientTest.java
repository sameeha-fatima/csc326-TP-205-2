package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Tests Rest API with Ingredient uses Spring boot and Hibernate
 *
 * @author Ellie Murphy
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	/** context for mvc object */
	@Autowired
	private WebApplicationContext context;

	/** IngredientService object for testing */
	@Autowired
	private IngredientService service;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		service.deleteAll();
	}

	/**
	 * Tests getIngredient()
	 *
	 * @throws Exception if there's an issue during testing
	 */
	@Test
	@Transactional
	public void testGetIngredient() throws Exception {
//		service.deleteAll();
		assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Ingredient i = new Ingredient("Sugar", 10);
//		service.save(i);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i)))
				.andExpect(status().isOk());
		final Ingredient i2 = new Ingredient("Milk", 15);
//		service.save(i2);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i2)))
				.andExpect(status().isOk());
		final Ingredient i3 = new Ingredient("Coffee", 20);
//		service.save(i3);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i3)))
				.andExpect(status().isOk());

		assertEquals(3, service.count());
		final String ingredient = mvc.perform(get(String.format("/api/v1/ingredients/%s", "Sugar"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertTrue(ingredient.contains("Sugar"));

		final String ingredient2 = mvc.perform(get(String.format("/api/v1/ingredients/%s", "Milk"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertTrue(ingredient2.contains("Milk"));

		final String ingredient3 = mvc.perform(get(String.format("/api/v1/ingredients/%s", "Coffee"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertTrue(ingredient3.contains("Coffee"));

		mvc.perform(get(String.format("/api/v1/ingredients/%s", "Chocolate"))).andDo(print())
				.andExpect(status().is4xxClientError());

		// deleting an ingredient and trying to get it
		mvc.perform(delete(String.format("/api/v1/ingredients/%s", "Sugar")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mvc.perform(get(String.format("/api/v1/ingredients/%s", "Sugar"))).andDo(print())
				.andExpect(status().is4xxClientError());

		// adding the ingredient back in after deleting, and getting it
//		service.save(i);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i)))
				.andExpect(status().isOk());
		final String ingredient4 = mvc.perform(get(String.format("/api/v1/ingredients/%s", "Sugar"))).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertTrue(ingredient4.contains("Sugar"));
	}

	/**
	 * Tests createIngredient()
	 *
	 * @throws Exception                    if there's an issue during testing
	 * @throws UnsupportedEncodingException if unsupported encoding is detected
	 */
	@Test
	@Transactional
	public void testCreateIngredient() throws UnsupportedEncodingException, Exception {
//		service.deleteAll();
		assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Ingredient i = new Ingredient("Honey", 10);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i)))
				.andExpect(status().isOk());
		final Ingredient i2 = new Ingredient("Vanilla", 15);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i2)))
				.andExpect(status().isOk());

		assertEquals(2, service.findAll().size());

		// test adding a duplicate name
		final Ingredient duplicate = new Ingredient("Honey", 50);
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(duplicate))).andExpect(status().is4xxClientError());
		assertEquals(2, service.findAll().size(), "There should only be two ingredients in the CoffeeMaker");

		// test adding an ingredient with invalid amount
//        final Ingredient i3 = new Ingredient( "Milk", -10 );
//        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
//                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().is4xxClientError() );
//        assertEquals( 2, service.findAll().size(), "There should only be two ingredients in the CoffeeMaker" );
	}

	/**
	 * Tests deleteIngredient()
	 *
	 * @throws Exception if there's an issue during testing
	 */
	@Test
	@Transactional
	public void testDeleteIngredient() throws Exception {
		service.deleteAll();
		assertEquals(0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker");
		assertEquals(null, service.findById(null));

		final Ingredient i = new Ingredient("Honey", 10);
//		service.save(i);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i)))
				.andExpect(status().isOk());
		final Ingredient i2 = new Ingredient("Vanilla", 15);
//		service.save(i2);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i2)))
				.andExpect(status().isOk());
		final Ingredient i3 = new Ingredient("Milk", 25);
//		service.save(i3);
		mvc.perform(
				post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(i3)))
				.andExpect(status().isOk());

		assertEquals(3, service.findAll().size());
		mvc.perform(delete(String.format("/api/v1/ingredients/%s", "Honey")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertEquals(2, service.findAll().size());

		mvc.perform(delete(String.format("/api/v1/ingredients/%s", "Milk")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertEquals(1, service.findAll().size());

		// test deleting an ingredient that does not exist
		mvc.perform(delete(String.format("/api/v1/ingredients/%s", "Sugar"))).andDo(print())
				.andExpect(status().is4xxClientError());

		mvc.perform(delete(String.format("/api/v1/ingredients/%s", "Vanilla")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertEquals(0, service.findAll().size());

	}

}
