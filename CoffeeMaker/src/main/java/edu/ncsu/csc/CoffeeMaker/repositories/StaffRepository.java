package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Staff;

/**
 * UserRepository is used to provide CRUD operations for the User model. Spring
 * will generate appropriate code with JPA.
 *
 * @author Cathy Sun (cqsun)
 *
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {
	/**
	 * Finds a User object with the provided username. Spring will generate code to
	 * make this happen.
	 *
	 * @param username Username of the User
	 * @return Found User, null if none.
	 */
	Staff findByName(String username);
}
