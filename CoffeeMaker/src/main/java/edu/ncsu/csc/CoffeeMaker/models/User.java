package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

/**
 * Abstract class representing a User in the CoffeeMaker system. This class is a
 * shared type that is used for all users of the CoffeeMaker system and handles
 * basic functionality such as authenticating the user in the system.
 *
 * @author Ellie Murphy
 *
 */
@Entity
//@JsonIgnoreProperties(value = { "password" })
public class User extends DomainObject {

	/** Recipe id */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Represents the username identifier for a User object, must be between 6 and
	 * 20 characters
	 */
	@Length(min = 6, max = 20)
	private String name;

	/**
	 * Represents the password for a User, must be between 6 an 20 characters
	 */
	@Length(min = 6, max = 20)
	private String password;

	/**
	 * Represents type of user account
	 */
	@Enumerated(EnumType.STRING)
	private UserEnum userType;

	/**
	 * Creates a default user for the CoffeeMaker system. Used by Hibernate
	 */
	public User() {
		super();
	}

	/**
	 * All-argument constructor for User
	 *
	 * @param name     represents username of User object
	 * @param password represents password of User object
	 * @param userType Enumeration representing type of user account
	 */
	public User(final String name, final String password, final String userType) {
		this();
		setPassword(password);
		setUserType(userType);
		setName(name);
	}

//	/**
//	 * All-argument constructor for User
//	 *
//	 * @param name     represents username of User object
//	 * @param password represents password of User object
//	 * @param userType String representing type of user account
//	 */
//	public User(final String name, final String password, final String userType) {
//		this();
//		setPassword(password);
//		this.userType = "customer".equalsIgnoreCase(userType) ? UserEnum.CUSTOMER : UserEnum.STAFF;
//		setName(name);
//	}

	/**
	 * Returns username of current user
	 *
	 * @return username of current user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns password for current user
	 *
	 * @return password for current user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the username for the current user. Throw an IllegalArgument exception if
	 * provided String is null, empty, is not between 6-20 characters, or contains
	 * characters other than the permitted alphanumeric set of characters
	 *
	 * @param username represents the username to set the current user's username to
	 * @throws IllegalArgumentException if provided username is a null or empty
	 *                                  String
	 * @throws IllegalArgumentException if the username is not of the required
	 *                                  length
	 * @throws IllegalArgumentException if provided username contains a character
	 *                                  other than the permitted alphanumeric
	 *                                  symbols
	 */
	public void setName(final String username) {
		if (username == null || "".equals(username)) {
			throw new IllegalArgumentException("User needs a username.");
		}
		if (username.length() < 6 || username.length() > 20) {
			throw new IllegalArgumentException("Invalid username length.");
		}
		if (!username.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("Invalid characters in username.");
		}

		this.name = username;
	}

	/**
	 * Sets the password for the current user. Throw an IllegalArgument exception if
	 * provided String is null, empty, is not between 6-20 characters, or contains
	 * characters other than the permitted alphanumeric, '?', '!', '$', '@' set of
	 * characters
	 *
	 * @param password represents the password to set the current user's password to
	 * @throws IllegalArgumentException if provided password is a null or empty
	 * @throws IllegalArgumentException if the username is not of the required
	 *                                  length String
	 * @throws IllegalArgumentException if provided password contains a character
	 *                                  other than the permitted alphanumeric
	 *                                  symbols
	 */
	public void setPassword(final String password) {
		if (password == null) {
			throw new IllegalArgumentException("User needs a password.");
		}
		if (password.length() < 6 || password.length() > 20) {
			throw new IllegalArgumentException("Invalid password length.");
		}
		if (!password.matches("^[a-zA-Z0-9!?$@]*$")) {
			throw new IllegalArgumentException("Invalid characters in password.");
		}

		this.password = password;
	}

	@Override
	public Serializable getId() {
		return this.id;
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
	 * Returns userType of current user
	 *
	 * @return userTypeof current user
	 */
	public UserEnum getUserType() {
		return userType;
	}

	/**
	 * Returns password for current user
	 *
	 * @param userType Enumeration representing type of user account
	 */

	public void setUserType(final String userType) {
		this.userType = "customer".equalsIgnoreCase(userType) ? UserEnum.CUSTOMER : UserEnum.STAFF;

	}

	@Override
	public int hashCode() {
		return Objects.hash(password, name);
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
		final User other = (User) obj;
		return Objects.equals(password, other.password) && Objects.equals(name, other.name);
	}
}
