package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

	/** Recipe id */
	@Id
	@GeneratedValue
	private Long id;

	/** Recipe name */
	private String name;

	/** Recipe price */
	@Min(0)
	private Integer price;

	/** Represents the list of ingredients in a recipe */
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private final List<Ingredient> ingredients;

	/**
	 * Creates a default recipe for the coffee maker.
	 */
	public Recipe() {
		this.name = "";
		ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Constructs a Recipe object given a list of Ingredients and an Integer
	 * representing the price
	 *
	 * @param ingredients List of Ingredient objects to represent the ingredients
	 *                    that make up a recipe
	 * @param price       Integer representing the price of a recipe
	 */
	public Recipe(final List<Ingredient> ingredients, final Integer price) {
		this();
		for (final Ingredient i : ingredients) {
			this.addIngredients(i);
		}
		setPrice(price);
	}

	/**
	 * Adds an ingredient to the Recipe object
	 *
	 * @param i represents Ingredient object to add to Recipe
	 */
	public void addIngredients(final Ingredient i) {
		for (final Ingredient curr : ingredients) {
			if (curr.isDuplicate(i)) {
				throw new IllegalArgumentException("Ingredient already exists.");
			}
		}
		if (i.getAmount() > 0) {
			ingredients.add(i);
		}
	}

//	/*
//	 * Edits the specified recipe with provided price and ingredients. Users can
//	 * add, remove, or adjust units of ingredients
//	 *
//	 * @param r represents Recipe object to update
//	 *
//	 * @param price represents updated price of Recipe
//	 *
//	 * @param ingredients represents updated list of ingredients with, potentially,
//	 * new units
//	 */
//	public void editRecipe(final Integer price, final List<Ingredient> ingredients) {
//		final Recipe newRecipe = new Recipe(ingredients, price);
//		newRecipe.setName(this.getName());
//		updateRecipe(newRecipe);
//	}

	/**
	 * Edits the current Recipe object to match the provided Recipe object
	 *
	 * @param newRecipe represent Recipe object with updated price and list of
	 *                  Ingredient objects
	 */
	public void editRecipe(final Recipe newRecipe) {
//		final Recipe newRecipe = new Recipe(ingredients, price);
		newRecipe.setName(this.getName());
		updateRecipe(newRecipe);
	}

	/**
	 * Returns a list of Ingredients in Recipe
	 *
	 * @return a list of Ingredients in Recipe
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Returns a specific Ingredient object amount from the Recipe based on the name
	 * of the ingredient or 0 if the Ingredient doesn't exist in the Recipe
	 *
	 * @param name represents name of Ingredient object to return
	 * @return Ingredient amount from Recipe based on provided Ingredient name or 0
	 *         if Ingredient doesn't exist
	 */
	public Integer getIngredients(final String name) {
		Integer amount = 0;
		for (int j = 0; j < ingredients.size(); j++) {
			final Ingredient curr = ingredients.get(j);
			if (curr.getName().equalsIgnoreCase(name)) {
				amount = curr.getAmount();
			}
		}
		return amount;
	}

//	private boolean isDuplicate(final Ingredient i) {
//		for (int j = 0; j < ingredients.size(); j++) {
//			if (ingredients.get(j).getName().equalsIgnoreCase(i.getName())) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * Updates the Ingredient amount represented by the provided name
	 *
	 * @param name   represents name of Ingredient object in Recipe to update
	 * @param amount represents updated amount for Ingredient
	 */
	public void updateIngredient(final String name, final Integer amount) {
		for (int j = 0; j < ingredients.size(); j++) {
			if (ingredients.get(j).getName().equalsIgnoreCase(name)) {
				ingredients.get(j).setAmount(amount);
			}
		}
	}

	/**
	 * Check if ingredient fields in the recipe are 0
	 *
	 * @return true if there are no ingredients, otherwise return false
	 */
	public boolean checkRecipe() {
		return ingredients.size() == 0;
	}

	/**
	 * Get the ID of the Recipe
	 *
	 * @return the ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Recipe (Used by Hibernate)
	 *
	 * @param id the ID
	 */
	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Returns name of the recipe.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the recipe name.
	 *
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the price of the recipe.
	 *
	 * @return Returns the price.
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Sets the recipe price.
	 *
	 * @param price The price to set.
	 */
	public void setPrice(final Integer price) {
		if (price < 0) {
			throw new IllegalArgumentException("Invalid price. Must be a positive Integer");
		}
		this.price = price;
	}

	/**
	 * Updates the fields to be equal to the passed Recipe
	 *
	 * @param r with updated fields
	 */
	public void updateRecipe(final Recipe r) {
		this.ingredients.clear();
		final List<Ingredient> newIngredients = r.getIngredients();
//		this.ingredients.addAll(newIngredients);
		for (int i = 0; i < newIngredients.size(); i++) {
			addIngredients(newIngredients.get(i));
		}
		setPrice(r.getPrice());
	}

	/**
	 * Returns the name of the recipe.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		return "" + name + ": " + ingredients.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		Integer result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Recipe other = (Recipe) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}