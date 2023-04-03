package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the User class
 *
 * @author Cathy Sun
 *
 */
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class UserTest {

	@Autowired
	private UserService service;

	// private static final String USER1 = "user1";
	//
	// private static final String USER2 = "user2";
	//
	// private static final String USER3 = "user3";
	//
	// private static final String PW = "abc123";

	/**
	 * Set up
	 */
	@BeforeEach
	public void setup() {
		service.deleteAll();
	}

	/**
	 * Tests user objects
	 */
	@Test
	@Transactional
	public void testUser() {
		assertEquals(0, service.count());
		final User c1 = new Customer("user1", "1111");
		assertEquals(c1.getPassowrd(), "1111");

	}

}