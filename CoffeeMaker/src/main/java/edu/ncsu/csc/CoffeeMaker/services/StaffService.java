package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * User repository to be used with User API
 *
 * @author Cathy Sun (cqsun)
 *
 */
@Component
@Transactional
public class StaffService extends Service<Staff, Long> {

	/**
	 * UserRepository, to be autowired in by Spring and provide CRUD operations on
	 * User model.
	 */
	@Autowired
	private StaffRepository staffRepository;

	@Override
	protected JpaRepository<Staff, Long> getRepository() {
		return staffRepository;
	}

	/**
	 * Find a user with the provided name
	 *
	 * @param name Name of the user to find
	 * @return found user, null if none
	 */
	public Staff findByName(final String name) {
		return staffRepository.findByName(name);
	}
}
