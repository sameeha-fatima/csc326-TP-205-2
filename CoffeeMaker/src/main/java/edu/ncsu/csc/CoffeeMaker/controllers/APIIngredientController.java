package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Ellie Murphy
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APIIngredientController extends APIController {

	/**
	 * IngredientService object, to be autowired in by Spring to allow for
	 * manipulating the Ingredient model
	 */
	@Autowired
	private IngredientService service;

	/**
	 * Creates a REST API method for returning ingredients in CoffeeMaker
	 *
	 * @return ingredients in CoffeeMaker
	 */
	@GetMapping(BASE_PATH + "/ingredients")
	public List<Ingredient> getIngredients() {
		return service.findAll();
	}

	/**
	 * Creates a REST API method for returning a specific ingredient from
	 * ingredients in CoffeeMaker given the name
	 *
	 * @param name represents the name of the ingredient to return
	 *
	 * @return ingredient in CoffeeMaker
	 */
	@GetMapping(BASE_PATH + "/ingredients/{name}")
	public ResponseEntity getIngredient(@PathVariable("name") final String name) {
		final Ingredient i = service.findByName(name);
		return null == i
				? new ResponseEntity(errorResponse("No ingredient found with name " + name), HttpStatus.NOT_FOUND)
				: new ResponseEntity(i, HttpStatus.OK);
	}

	/**
	 * REST API method to provide POST access to the Recipe model. This is used to
	 * create a new Recipe by automatically converting the JSON RequestBody provided
	 * to a Recipe object. Invalid JSON will fail.
	 *
	 * @param i represents ingredient to be added to CoffeeMaker.
	 * @return ResponseEntity indicating success if the Recipe could be saved to the
	 *         inventory, or an error if it could not be
	 */
	@PostMapping(BASE_PATH + "/ingredients")
	public ResponseEntity createIngredient(@RequestBody final Ingredient i) {
		final Ingredient db = service.findByName(i.getName());
		if (null != db) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}

		if (i.getAmount() > 0) {
			service.save(i);
			return new ResponseEntity(successResponse(i.getName() + " successfully created"), HttpStatus.OK);
		} else {
			return new ResponseEntity(errorResponse("Insufficient amount for ingredient " + i.getName()),
					HttpStatus.INSUFFICIENT_STORAGE);
		}

	}

	/**
	 * REST API method to allow deleting a Recipe from the CoffeeMaker's Inventory,
	 * by making a DELETE request to the API endpoint and indicating the recipe to
	 * delete (as a path variable)
	 *
	 * @param name The name of the Ingredient to delete
	 * @return Success if the recipe could be deleted; an error if the recipe does
	 *         not exist
	 */
	@DeleteMapping(BASE_PATH + "/ingredients/{name}")
	public ResponseEntity deleteIngredient(@PathVariable final String name) {
		final Ingredient i = service.findByName(name);
		if (null == i) {
			return new ResponseEntity(errorResponse("No recipe found for name " + name), HttpStatus.NOT_FOUND);
		}
		service.delete(i);

		return new ResponseEntity(successResponse(name + " was deleted successfully"), HttpStatus.OK);
	}
}
