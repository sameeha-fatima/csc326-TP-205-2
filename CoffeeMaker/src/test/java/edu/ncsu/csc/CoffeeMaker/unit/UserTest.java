package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the User class
 *
 * @author Cathy Sun
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UserTest {

    /** Represents the service class used to access User database */
    @Autowired
    private UserService service;

    // private static final String USER1 = "user1";
    //
    // private static final String USER2 = "user2";
    //
    // private static final String USER3 = "user3";
    //
    // private static final String PW = "abc123";

    /**
     * Set up
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests valid user objects of both customer and staff
     */
    @Test
    @Transactional
    public void testUserValid () {

        // testing creating a customer object
        assertEquals( 0, service.count() );
        final User c1 = new Customer( "customer1", "1111" );
        // service.save( c1 );
        assertEquals( c1.getUsername(), "customer1" );
        assertEquals( c1.getPassword(), "1111" );
        // assertEquals( 1, service.count() );

        final User c2 = new Customer( "customer2", "2222" );
        // service.save( c1 );
        assertEquals( c2.getUsername(), "customer2" );
        assertEquals( c2.getPassword(), "2222" );

        // testing creating a staff object
        final User s1 = new Staff( "staff1", "1111" );
        // service.save( c1 );
        assertEquals( s1.getUsername(), "staff1" );
        assertEquals( s1.getPassword(), "1111" );
        // assertEquals( 1, service.count() );

        final User s2 = new Staff( "staff2", "2222" );
        // service.save( c1 );
        assertEquals( s2.getUsername(), "staff2" );
        assertEquals( s2.getPassword(), "2222" );

    }

    /**
     * Tests valid user objects of both customer and staff
     */
    @Test
    @Transactional
    public void testUserInValid () {
        // TODO: Add length checks for password and username

        // invalid customer username length
        final User c1 = new Customer( "customer1", "1" );
        // assertEquals( c1.getPassowrd(), "1" );
        // final Exception e1 = assertThrows( IllegalArgumentException.class, ()
        // -> c1.setPassword( "1" ) );
        // assertEquals( "Invalid Ingredient amount.", e1.getMessage() );
    }

}
