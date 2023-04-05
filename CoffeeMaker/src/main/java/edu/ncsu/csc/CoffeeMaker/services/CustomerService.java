package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;

/**
 * User repository to be used with User API
 *
 * @author Cathy Sun (cqsun)
 *
 */
@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

	/**
	 * UserRepository, to be autowired in by Spring and provide CRUD operations on
	 * User model.
	 */
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		return customerRepository;
	}

	/**
	 * Find a user with the provided name
	 *
	 * @param name Name of the user to find
	 * @return found user, null if none
	 */
	public Customer findByName(final String name) {
		return customerRepository.findByName(name);
	}
}
