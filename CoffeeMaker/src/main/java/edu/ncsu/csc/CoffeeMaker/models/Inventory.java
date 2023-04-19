package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

	/** id for inventory entry */
	@Id
	@GeneratedValue
	private Long id;
//	/** amount of coffee */
//	// @Min(0)
//	@OneToOne(fetch = FetchType.EAGER)
//	private Ingredient coffee;
//	/** amount of milk */
//	// @Min(0)
//	@OneToOne(fetch = FetchType.EAGER)
//	private Ingredient milk;
//	/** amount of sugar */
//	// @Min(0)
//	@OneToOne(fetch = FetchType.EAGER)
//	private Ingredient sugar;
//	/** amount of chocolate */
//	// @Min(0)
//	@OneToOne(fetch = FetchType.EAGER)
//	private Ingredient chocolate;

	/** list of ingredients currently in inventory */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Ingredient> ingredient;

	// /** amount of ant ingredient */
	// @OneToOne ( fetch = FetchType.EAGER )
	// private Integer amt;

	/**
	 * Empty constructor for Hibernate
	 */
	public Inventory() {
		// Intentionally empty so that Hibernate can instantiate
		// Inventory object.
	}

	/**
	 * Constructor for creating Inventory based on a provided list of ingredients
	 *
	 * @param ingredients represents the user-defined list of ingredients to add
	 */
	public Inventory(final List<Ingredient> ingredients) {
		this.ingredient = new ArrayList<Ingredient>();
		for (int i = 0; i < ingredients.size(); i++) {
			this.ingredient.add(ingredients.get(i));
		}
	}

	/**
	 * Returns the amount, in inventory, for the specified ingredient object
	 *
	 * @param name represents Ingredient object to be returned
	 * @return amount of ingredient in inventory, or 0 if the ingredient doesn't
	 *         exist
	 */
	public Integer getIngredientAmount(final String name) {
		Integer a = 0;
		for (int j = 0; j < ingredient.size(); j++) {
			if (name.equalsIgnoreCase(ingredient.get(j).getName())) {
				a = ingredient.get(j).getAmount();
			}

		}
		return a;

	}

	/**
	 * Returns the ingredients in the inventory
	 *
	 * @return the ingredients in the inventory
	 */
	public List<Ingredient> getIngredient() {
		return ingredient;
	}

	/**
	 * Returns the ID of the entry in the DB
	 *
	 * @return long
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Inventory (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Checks if there are enough ingredients currently in inventory to make the
	 * provided recipe
	 *
	 * @param r represent recipe whose ingredients will be compared against
	 *          inventory
	 * @return true if there are enough ingredients and false if not
	 */
	public boolean enoughIngredients(final Recipe r) {
		final boolean isEnough = true;
		for (int i = 0; i < r.getIngredients().size(); i++) {
			final Ingredient currentIngredient = r.getIngredients().get(i);
			for (int j = 0; j < ingredient.size(); j++) {
				final Ingredient inInventory = ingredient.get(j);
				if (currentIngredient.getName().equalsIgnoreCase(inInventory.getName())) {
					if (currentIngredient.getAmount() > inInventory.getAmount()) {
						return false;
					}
				}
			}
		}

		return isEnough;
	}

	/**
	 * Removes as many ingredients from inventory as are in the provided recipe
	 *
	 * @param r represents recipe with ingredient amounts to be removed from
	 *          inventory
	 * @return true if ingredients are successfully removed and false if there was
	 *         an issue
	 */
	public boolean useIngredients(final Recipe r) {
		if (enoughIngredients(r)) {
			for (final Ingredient i : r.getIngredients()) { // iterate through ingredients in recipe
				final String name = i.getName();
				for (final Ingredient invI : ingredient) { // iterate through inventory
					if (name.equalsIgnoreCase(invI.getName())) {
						invI.setAmount(invI.getAmount() - i.getAmount());
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds ingredients to the inventory given an Ingredient's name and amount.
	 * Creates an Ingredient object based on parameter values
	 *
	 * @param name   represents name of new Ingredient object
	 * @param amount represents amount of new Ingredient object
	 * @return true if successfully able to add ingredient to inventory, false if
	 *         not
	 */
	public boolean addIngredient(final String name, final Integer amount) {
		if (amount < 0) {
			return false;
		}
		final boolean found = setIngredient(name, amount);
		if (!found) {
			ingredient.add(new Ingredient(name, amount));
		}
		return true;
	}

	/**
	 * Adds ingredients to the inventory
	 *
	 * @param i represents Ingredient object to add to inventory
	 * @return true if successful, false if not
	 */
	public boolean addIngredient(final Ingredient i) {
//		ingredients.add(i);
		if (i.getAmount() < 0) {
			return false;
		}
		final boolean found = setIngredient(i.getName(), i.getAmount());
		if (!found) {
			ingredient.add(i);
		}
		return true;
	}

	/**
	 * Updates the amount of an ingredient given its name. If the ingredient cannot
	 * be found in the inventory, the method returns false to indicate it does not
	 * exist, otherwise returns true
	 *
	 * @param name   represents the name of the ingredient to update
	 * @param amount represents the amount to update the ingredient to
	 * @return boolean representing whether the ingredient was successfully updated
	 */
	private boolean setIngredient(final String name, final Integer amount) {
		for (final Ingredient i : ingredient) {
			if (i.getName().equalsIgnoreCase(name)) {
				i.setAmount(amount + i.getAmount());
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string describing the current contents of the inventory.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		for (final Ingredient curr : ingredient) {
			buf.append("" + curr.getName() + ": " + curr.getAmount() + "\n");
		}
		return buf.toString();
	}

//	/* Proceed to the comment graveyard */

//	/**
//	 * Returns the current number of chocolate units in the inventory.
//	 *
//	 * @return amount of chocolate
//	 */
//	public Integer getChocolate() {
//		return chocolate.getAmount();
//	}

//	/**
//	 * Sets the number of chocolate units in the inventory to the specified amount.
//	 *
//	 * @param amtChocolate amount of chocolate to set
//	 */
//	public void setChocolate(final Integer amtChocolate) {
//		if (amtChocolate >= 0) {
//			chocolate.setAmount(amtChocolate);
//			;
//		}
//	}

//	/**
//	 * Add the number of chocolate units in the inventory to the current amount of
//	 * chocolate units.
//	 *
//	 * @param chocolate amount of chocolate
//	 * @return checked amount of chocolate
//	 * @throws IllegalArgumentException if the parameter isn't a positive integer
//	 */
//	public Integer checkChocolate(final String chocolate) throws IllegalArgumentException {
//		Integer amtChocolate = 0;
//		try {
//			amtChocolate = Integer.parseInt(chocolate);
//		} catch (final NumberFormatException e) {
//			throw new IllegalArgumentException("Units of chocolate must be a positive integer");
//		}
//		if (amtChocolate < 0) {
//			throw new IllegalArgumentException("Units of chocolate must be a positive integer");
//		}
//
//		return amtChocolate;
//	}
//
//	/**
//	 * Returns the current number of coffee units in the inventory.
//	 *
//	 * @return amount of coffee
//	 */
//	public Integer getCoffee() {
//		return coffee.getAmount();
//	}
//
//	/**
//	 * Sets the number of coffee units in the inventory to the specified amount.
//	 *
//	 * @param amtCoffee amount of coffee to set
//	 */
//	public void setCoffee(final Integer amtCoffee) {
//		if (amtCoffee >= 0) {
//			coffee.setAmount(amtCoffee);
//		}
//	}

//	/**
//	 * Add the number of coffee units in the inventory to the current amount of
//	 * coffee units.
//	 *
//	 * @param coffee amount of coffee
//	 * @return checked amount of coffee
//	 * @throws IllegalArgumentException if the parameter isn't a positive integer
//	 */
//	public Integer checkCoffee(final String coffee) throws IllegalArgumentException {
//		Integer amtCoffee = 0;
//		try {
//			amtCoffee = Integer.parseInt(coffee);
//		} catch (final NumberFormatException e) {
//			throw new IllegalArgumentException("Units of coffee must be a positive integer");
//		}
//		if (amtCoffee < 0) {
//			throw new IllegalArgumentException("Units of coffee must be a positive integer");
//		}
//
//		return amtCoffee;
//	}
//
//	/**
//	 * Returns the current number of milk units in the inventory.
//	 *
//	 * @return int
//	 */
//	public Integer getMilk() {
//		return milk.getAmount();
//	}
//
//	/**
//	 * Sets the number of milk units in the inventory to the specified amount.
//	 *
//	 * @param amtMilk amount of milk to set
//	 */
//	public void setMilk(final Integer amtMilk) {
//		if (amtMilk >= 0) {
//			milk.setAmount(amtMilk);
//		}
//	}
//
//	/**
//	 * Add the number of milk units in the inventory to the current amount of milk
//	 * units.
//	 *
//	 * @param milk amount of milk
//	 * @return checked amount of milk
//	 * @throws IllegalArgumentException if the parameter isn't a positive integer
//	 */
//	public Integer checkMilk(final String milk) throws IllegalArgumentException {
//		Integer amtMilk = 0;
//		try {
//			amtMilk = Integer.parseInt(milk);
//		} catch (final NumberFormatException e) {
//			throw new IllegalArgumentException("Units of milk must be a positive integer");
//		}
//		if (amtMilk < 0) {
//			throw new IllegalArgumentException("Units of milk must be a positive integer");
//		}
//
//		return amtMilk;
//	}
//
//	/**
//	 * Returns the current number of sugar units in the inventory.
//	 *
//	 * @return int
//	 */
//	public Integer getSugar() {
//		return sugar.getAmount();
//	}
//
//	/**
//	 * Sets the number of sugar units in the inventory to the specified amount.
//	 *
//	 * @param amtSugar amount of sugar to set
//	 */
//	public void setSugar(final Integer amtSugar) {
//		if (amtSugar >= 0) {
//			sugar.setAmount(amtSugar);
//		}
//	}
//
//	/**
//	 * Add the number of sugar units in the inventory to the current amount of sugar
//	 * units.
//	 *
//	 * @param sugar amount of sugar
//	 * @return checked amount of sugar
//	 * @throws IllegalArgumentException if the parameter isn't a positive integer
//	 */
//	public Integer checkSugar(final String sugar) throws IllegalArgumentException {
//		Integer amtSugar = 0;
//		try {
//			amtSugar = Integer.parseInt(sugar);
//		} catch (final NumberFormatException e) {
//			throw new IllegalArgumentException("Units of sugar must be a positive integer");
//		}
//		if (amtSugar < 0) {
//			throw new IllegalArgumentException("Units of sugar must be a positive integer");
//		}
//
//		return amtSugar;
//	}
//
//	/**
//	 * Returns true if there are enough ingredients to make the beverage.
//	 *
//	 * @param r recipe to check if there are enough ingredients
//	 * @return true if enough ingredients to make the beverage
//	 */
	// public boolean enoughIngredients ( final Recipe r ) {
	// boolean isEnough = true;
	// // iterate through each of the ingredients needed by the recipe and
	// // check if the necessary ingredients have enough inventory
	// // todo: implementation is limited to milk, sugar, coffee, and chocolate
	// for ( final Ingredient i : r.getIngredients() ) {
	// if ( i.getName().equalsIgnoreCase( "Coffee" ) ) {
	// if ( getCoffee() < i.getAmount() ) {
	// isEnough = false;
	// }
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Milk" ) ) {
	// if ( getMilk() < i.getAmount() ) {
	// isEnough = false;
	// }
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Sugar" ) ) {
	// if ( getSugar() < i.getAmount() ) {
	// isEnough = false;
	// }
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Chocolate" ) ) {
	// if ( getChocolate() < i.getAmount() ) {
	// isEnough = false;
	// }
	// }
	// }
	//
	// // if (milk < r.getMilk()) {
	// // isEnough = false;
	// // }
	// // if (sugar < r.getSugar()) {
	// // isEnough = false;
	// // }
	// // if (chocolate < r.getChocolate()) {
	// // isEnough = false;
	// // }
	// return isEnough;
	// }

//	/**
//	 * Removes the ingredients used to make the specified recipe. Assumes that the
//	 * user has checked that there are enough ingredients to make
//	 *
//	 * @param r recipe to make
//	 * @return true if recipe is made.
//	 */
	// public boolean useIngredients ( final Recipe r ) {
	// if ( enoughIngredients( r ) ) {
	// for ( final Ingredient i : r.getIngredients() ) {
	// if ( i.getName().equalsIgnoreCase( "Coffee" ) ) {
	// setCoffee( getCoffee() - i.getAmount() );
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Milk" ) ) {
	// setMilk( getMilk() - i.getAmount() );
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Sugar" ) ) {
	// setSugar( getSugar() - i.getAmount() );
	// }
	//
	// if ( i.getName().equalsIgnoreCase( "Chocolate" ) ) {
	// setChocolate( getChocolate() - i.getAmount() );
	// }
	// }
	// // setCoffee(coffee - r.getCoffee());
	// // setMilk(milk - r.getMilk());
	// // setSugar(sugar - r.getSugar());
	// // setChocolate(chocolate - r.getChocolate());
	// return true;
	// }
	// else {
	// return false;
	// }
	// }

	// /**
	// * Adds ingredients to the inventory
	// *
	// * @param coffee amt of coffee
	// * @param milk amt of milk
	// * @param sugar amt of sugar
	// * @param chocolate amt of chocolate
	// * @return true if successful, false if not
	// */
	// public boolean addIngredients(final String name, final Integer amount) {
	//// if (coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0) {
	//// throw new IllegalArgumentException("Amount cannot be negative");
	//// }
	////
	//// setCoffee(getCoffee() + coffee);
	//// setMilk(getMilk() + milk);
	//// setSugar(getSugar() + sugar);
	//// setChocolate(getChocolate() + chocolate);
	//
	// if (amount < 0) {
	// throw new IllegalArgumentException("Amount cannot be negative");
	// }
	// return setIngredient(name, amount);
	//// return true;
	// }

}
