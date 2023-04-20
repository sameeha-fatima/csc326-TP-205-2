package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

// import org.junit.jupiter.api.Assertions;
/**
 * Tests arbitrary Ingredient objects
 *
 * @author Ellie Murphy
 *
 */
public class IngredientTest {

	/**
	 * Tests Ingredient.*Name()
	 */
	@Test
	@Transactional
	public void testIngredientName() {

		final Ingredient i = new Ingredient();

		assertNull(i.getName());
		i.setName("Latte");
		assertEquals("Latte", i.getName());
		i.setName("Not a Latte");
		assertEquals("Not a Latte", i.getName());

		final Ingredient i2 = new Ingredient("Latte", 10);

		assertEquals("Latte", i2.getName());
		i2.setName("Not a Latte");
		assertEquals("Not a Latte", i2.getName());
	}

	/**
	 * Tests Ingredient.*Amount()
	 */
	@Test
	@Transactional
	public void testIngredientAmount() {

		final Ingredient i = new Ingredient();

		assertNull(i.getAmount());
		i.setAmount(10);
		assertEquals(10, i.getAmount());
		i.setAmount(2);
		assertEquals(2, i.getAmount());
		final Exception e1 = assertThrows(IllegalArgumentException.class, () -> i.setAmount(-2));
		assertEquals("Invalid Ingredient amount.", e1.getMessage());

		final Ingredient i2 = new Ingredient("Latte", 10);

		assertEquals(10, i2.getAmount());
		i2.setAmount(20);
		assertEquals(20, i2.getAmount());
	}

	/**
	 * Tests Ingredient.toString()
	 */
	@Test
	@Transactional
	public void testToString() {

		final Ingredient i = new Ingredient("Latte", 10);
		String s = "Ingredient [id=" + i.getId() + ", name=" + i.getName() + ", amount=" + i.getAmount() + "]";
		assertEquals(s, i.toString());
		i.setName("Mocha");
		s = "Ingredient [id=" + i.getId() + ", name=" + i.getName() + ", amount=" + i.getAmount() + "]";
		assertEquals(s, i.toString());
		i.setAmount(2);
		s = "Ingredient [id=" + i.getId() + ", name=" + i.getName() + ", amount=" + i.getAmount() + "]";
		assertEquals(s, i.toString());

		final Ingredient i2 = new Ingredient("", 10);
		s = "Ingredient [id=" + i2.getId() + ", name=" + i2.getName() + ", amount=" + i2.getAmount() + "]";
		assertEquals(s, i2.toString());
	}
}
