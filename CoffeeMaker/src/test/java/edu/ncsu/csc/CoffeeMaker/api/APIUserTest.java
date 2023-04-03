package edu.ncsu.csc.CoffeeMaker.api;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.services.UserService;

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
	private UserService service;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		service.deleteAll();
	}

	/**
	 * Tests login API call
	 *
	 * @throws Exception if exception thrown during testing
	 */
	@Test
	@Transactional
	public void testLogin() throws Exception {
//		final String user = mvc.perform(get("/api/v1/users")).andDo(print()).andExpect(status().isOk()).andReturn()
//				.getResponse().getContentAsString();
		// TODO
	}

}
