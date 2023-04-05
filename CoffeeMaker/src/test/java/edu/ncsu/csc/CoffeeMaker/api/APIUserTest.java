package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

/**
 * Tests User database interactions, including login functionality
 *
 * @author Ellie Murphy
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class APIUserTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	private MockMvc mvc;

	/** context for mvc object */
	@Autowired
	private WebApplicationContext context;

	/** UserService object for testing */
	@Autowired
	private StaffService sService;

	/** UserService object for testing */
	@Autowired
	private CustomerService cService;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		sService.deleteAll();
		cService.deleteAll();
	}

	/**
	 * Tests login API call
	 *
	 * @throws Exception if exception thrown during testing
	 */
	@Test
	@Transactional
	public void testCreateUser() throws Exception {
		sService.deleteAll();
		cService.deleteAll();

		assertEquals(0, sService.findAll().size());
		assertEquals(0, cService.findAll().size());
		final Customer customer = new Customer("customer1", "thisisagoodpassword");
		mvc.perform(
				post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(customer)))
				.andExpect(status().isOk());
		assertEquals(1, cService.findAll().size());
		final List<Customer> contents = cService.findAll();
		assertEquals(customer, contents.get(0));
	}

	/**
	 * Tests login API call
	 *
	 * @throws Exception if exception thrown during testing
	 */
	@Test
	@Transactional
	public void testLogin() throws Exception {
		sService.deleteAll();
		cService.deleteAll();

		final Customer customer = new Customer("customer1", "thisisagoodpassword");
		mvc.perform(
				post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(customer)))
				.andExpect(status().isOk());
		assertEquals(1, cService.findAll().size());

	}

}
