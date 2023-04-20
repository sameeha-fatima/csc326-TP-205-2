package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient for the coffee maker. Ingredient is tied to the database using
 * Hibernate libraries. See IngredientRepository and IngredientService for the
 * other two pieces used for database support.
 *
 * @author Ellie Murphy
 */
@Entity
public class Ingredient extends DomainObject {

	/** Ingredient id */
	@Id
	@GeneratedValue
	private Long id;

	// @Enumerated(EnumType.STRING)
	// private IngredientType ingredient;
	/** Ingredient name */
	private String name;

	/** Ingredient amount */
	@Min(0)
	private Integer amount;

	/** Constructs an Ingredient as an Entity */
	public Ingredient() {
		super();
	}

	// public Ingredient(final IngredientType ingredient, final Integer amount)
	// {
	/**
	 * Constructs an Ingredient with provided name and amount values
	 *
	 * @param name   represents name of new Ingredient
	 * @param amount represents amount of new Ingredient
	 */
	public Ingredient(final String name, final Integer amount) {
		super();
		setName(name);
		setAmount(amount);
	}

	@Override
	public Serializable getId() {
		return this.id;
	}

	// public IngredientType getIngredient() {
	// return ingredient;
	// }
	//
	// public void setIngredient(final IngredientType ingredient) {
	// this.ingredient = ingredient;
	// }

	/**
	 * Returns name of Ingredient
	 *
	 * @return name of Ingredient
	 */
	public String getName() {
		return name;
	}

	/**
	 * Updates the name of the Ingredient to provided String
	 *
	 * @param name with which to update the Ingredient
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns amount of Ingredient
	 *
	 * @return amount of Ingredient
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * Updates amount of Ingredient to provided Integer
	 *
	 * @param amount represents new amount to update Ingredient with
	 */
	public void setAmount(final Integer amount) {
		if (amount >= 0) {
			this.amount = amount;
		} else {
			throw new IllegalArgumentException("Invalid Ingredient amount.");
			// need to add conditionals to account for negative amount input
		}
	}

	/**
	 * Set the ID of the Ingredient (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + ", amount=" + amount + "]";
	}

	/**
	 * Checks if the current Ingredient has the same name as the provided Ingredient
	 *
	 * @param i represents Ingredient with which to compare name of current
	 *          Ingredient
	 * @return true if Ingredients have the same name, ignoring case, and false if
	 *         not
	 */
	public boolean isDuplicate(final Ingredient i) {
//		for (int j = 0; j < ingredients.size(); j++) {
		if (this.getName().equalsIgnoreCase(i.getName())) {
			return true;
		}
//		}
		return false;
	}

}
