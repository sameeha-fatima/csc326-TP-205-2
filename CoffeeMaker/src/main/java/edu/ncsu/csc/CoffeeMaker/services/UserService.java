package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * User repository to be used with User API
 *
 * @author Cathy Sun (cqsun)
 *
 */
@Component
@Transactional
public class UserService extends Service<User, Long> {

    /**
     * UserRepository, to be autowired in by Spring and provide CRUD operations
     * on User model.
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

    /**
     * Find a user with the provided name
     *
     * @param name
     *            Name of the user to find
     * @return found user, null if none
     */
    public User findByName ( final String name ) {
        return userRepository.findByName( name );
    }
}
