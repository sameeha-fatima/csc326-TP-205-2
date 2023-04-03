package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Class to handle Rest API calls dealing with the User class. To be used by the
 * HTML pages
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Audrey Si (fasi)
 *
 */
@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
public class APIUserController extends APIController {

	/**
	 * UserService object, to be autowired in by Spring to allow for manipulating
	 * the User model
	 */
	@Autowired
	private UserService userService;

	/**
	 * REST API method to provide GET access to all users in the system
	 *
	 * @param id represents the user ID / username used to find a user from the
	 *           database
	 * @return JSON representation of the found user or error message if none is
	 *         found
	 */
	@GetMapping(BASE_PATH + "/users/{id}")
	public ResponseEntity getUser(@PathVariable("id") final String id) {
		final User user = userService.findByName(id);
		return null == user
				? new ResponseEntity(errorResponse("No User found for username " + id), HttpStatus.NOT_FOUND)
				: new ResponseEntity(user, HttpStatus.OK);
	}

	/**
	 * REST API method to provide POST access to the User model. This is used to
	 * create a new User by automatically converting the JSON RequestBody provided
	 * to a User object. Invalid JSON will fail.
	 *
	 * @param user The valid User to be saved.
	 * @return ResponseEntity indicating success if the User could be saved to the
	 *         CoffeeMaker system, or an error if it could not be
	 */
	@PostMapping(BASE_PATH + "/users")
	public ResponseEntity createUser(@RequestBody final User user) {
		if (null != userService.findByName(user.getUsername())) {
			return new ResponseEntity(errorResponse("User with the id " + user.getUsername() + " already exists"),
					HttpStatus.CONFLICT);
		}
		final User newUser = null;
		try {
			userService.save(user);
			return new ResponseEntity(newUser, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity(
					errorResponse("Could not create " + user.getUsername() + " because of " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method used to login to a new user in the system. Return error message if
	 * there are errors during the login process
	 *
	 * @param user represents user to search for in the CoffeeMaker system
	 * @return User object with 200 status if successful login or an error response
	 *         (400) if not
	 */
	public ResponseEntity login(final User user) {
		final User foundUser = userService.findByName(user.getUsername());
		if (null == foundUser) {
			new ResponseEntity(errorResponse("No User found for username " + user.getUsername()), HttpStatus.NOT_FOUND);
		}
		final String inputPasswrd = user.getPassowrd();
		if (inputPasswrd.equals(foundUser.getPassowrd())) {
			return new ResponseEntity(user, HttpStatus.OK);
		}
		return new ResponseEntity(errorResponse("Password is incorrect"), HttpStatus.BAD_REQUEST);

	}

	/**
	 * REST API method to allow deleting a User from the CoffeeMaker's Inventory, by
	 * making a DELETE request to the API endpoint and indicating the User to delete
	 * (as a path variable)
	 *
	 * @param id The username of the User to delete
	 * @return Success if the User could be deleted; an error if the User does not
	 *         exist
	 */
	@DeleteMapping(BASE_PATH + "/users/{id}")
	public ResponseEntity deleteUser(@PathVariable final String id) {
		final User user = userService.findByName(id);
		try {
			if (null == user) {
				return new ResponseEntity(errorResponse("No user found for id " + id), HttpStatus.NOT_FOUND);
			}
			userService.delete(user);
			return new ResponseEntity(id, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity(errorResponse("Could not delete " + id + " because of " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	}
}
