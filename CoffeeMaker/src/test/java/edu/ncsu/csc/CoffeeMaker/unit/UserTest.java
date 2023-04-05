package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
        final User c1 = new Customer( "customer1", "iamapassword" );
        // service.save( c1 );
        assertEquals( c1.getUsername(), "customer1" );
        assertEquals( c1.getPassword(), "iamapassword" );
        // assertEquals( 1, service.count() );

        final User c2 = new Customer( "customer2", "iamapassword" );
        // service.save( c1 );
        assertEquals( c2.getUsername(), "customer2" );
        assertEquals( c2.getPassword(), "iamapassword" );

        // testing creating a staff object
        final User s1 = new Staff( "staff1", "iamapassword" );
        // service.save( c1 );
        assertEquals( s1.getUsername(), "staff1" );
        assertEquals( s1.getPassword(), "iamapassword" );
        // assertEquals( 1, service.count() );

        final User s2 = new Staff( "staff2", "iamapassword" );
        // service.save( c1 );
        assertEquals( s2.getUsername(), "staff2" );
        assertEquals( s2.getPassword(), "iamapassword" );

        final User s3 = new Staff( "staff2", "iamapassword!" );
        // service.save( c1 );
        assertEquals( s3.getUsername(), "staff2" );
        assertEquals( s3.getPassword(), "iamapassword!" );

    }

    /**
     * Tests valid user objects of both customer and staff
     */
    @Test
    @Transactional
    public void testInvalidLength () {
        // invalid customer password length
        try {
            new Customer( "customer1", "1" );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid password length.", e.getMessage() );
        }
        try {
            new Customer( "customer1", "iamapasswordandiamgreat" );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid password length.", e.getMessage() );
        }
        try {
            new Customer( "customer1", "iamapassword:" );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid characters in password.", e.getMessage() );
        }
        // invalid customer username length
        try {
            new Customer( "user", "iamapassword" );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid username length.", e.getMessage() );
        }
        try {
            new Customer( "customer!", "iamapassword" );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid characters in username.", e.getMessage() );
        }
    }

    /**
     * Tests user invalid username and passwords
     */
    @Test
    @Transactional
    public void testInvalidLength2 () {

        final User c1 = new Customer( "customer1", "111111" );

        // invalid customer username length
        Exception e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "c" ) );
        assertEquals( "Invalid username length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "custo" ) );
        assertEquals( "Invalid username length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "customer1111111111111111111" ) );
        assertEquals( "Invalid username length.", e.getMessage() );

        // invalid customer password length
        e = assertThrows( IllegalArgumentException.class, () -> c1.setPassword( "1" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setPassword( "11111" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setPassword( "111111111111111111111111" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
    }

    /**
     * Tests user invalid username and passwords
     */
    @Test
    @Transactional
    public void testCharacters () {

        // valid customer username
        final User c1 = new Customer( "customer1", "111111" );
        c1.setUsername( "customer" );
        assertEquals( c1.getUsername(), "customer" );
        c1.setUsername( "111111" );
        assertEquals( c1.getUsername(), "111111" );
        c1.setUsername( "CUSTOMER1" );
        assertEquals( c1.getUsername(), "CUSTOMER1" );
        c1.setUsername( "CuStOmEr1" );
        assertEquals( c1.getUsername(), "CuStOmEr1" );

        // invalid customer username
        Exception e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "username!!" ) );
        assertEquals( "Invalid characters in username.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "@_@?????" ) );
        assertEquals( "Invalid characters in username.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "$!@*??" ) );
        assertEquals( "Invalid characters in username.", e.getMessage() );

        // valid customer password
        final User c2 = new Customer( "customer2", "111111" );
        c2.setPassword( "aaaaaaaah" );
        assertEquals( c2.getPassword(), "aaaaaaaah" );
        c2.setPassword( "AAAAAAh" );
        assertEquals( c2.getPassword(), "AAAAAAh" );
        c2.setPassword( "111111" );
        assertEquals( c2.getPassword(), "111111" );
        c2.setPassword( "AaAaAaAah" );
        assertEquals( c2.getPassword(), "AaAaAaAah" );
        c2.setPassword( "KE$HA!" );
        assertEquals( c2.getPassword(), "KE$HA!" );
        c2.setPassword( "???????" );
        assertEquals( c2.getPassword(), "???????" );
        c2.setPassword( "?$o$!@o@?" );
        assertEquals( c2.getPassword(), "?$o$!@o@?" );
        // invalid customer password
        Exception e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "me: *_* x_x @_@" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "******" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( " o o o o o" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "---------" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "&&&&&&&&&" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( ":::::]]" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "comeon&#me" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "()+=/<>}" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
        e2 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "(=^W^=)(*o*)" ) );
        assertEquals( "Invalid characters in password.", e2.getMessage() );
    }

}
