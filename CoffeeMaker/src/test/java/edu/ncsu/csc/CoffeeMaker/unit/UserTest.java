package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
// import edu.ncsu.csc.CoffeeMaker.models.Customer;
// import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.UserEnum;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the User class with its child classes customer and staff
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
     * Tests creating user objects of both customer and staff
     */
    @Test
    @Transactional
    public void testUser () {

        // testing creating a customer object
        assertEquals( 0, service.count() );
        final User c1 = new User( "customer1", "iamapassword", UserEnum.CUSTOMER );
        // service.save( c1 );
        assertEquals( c1.getUsername(), "customer1" );
        assertEquals( c1.getPassword(), "iamapassword" );
        // assertEquals( 1, service.count() );

        final User c2 = new User( "customer2", "iamapassword", UserEnum.CUSTOMER );
        // service.save( c1 );
        assertEquals( c2.getUsername(), "customer2" );
        assertEquals( c2.getPassword(), "iamapassword" );

        // testing creating a customer object invalid
        Exception e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( null ) );
        assertEquals( "User needs a username.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> c1.setUsername( "" ) );
        assertEquals( "User needs a username.", e.getMessage() );

        // testing creating a staff object
        final User s1 = new User( "staff1", "iamapassword", UserEnum.STAFF );
        // service.save( c1 );
        assertEquals( s1.getUsername(), "staff1" );
        assertEquals( s1.getPassword(), "iamapassword" );
        // assertEquals( 1, service.count() );

        final User s2 = new User( "staff2", "iamapassword", UserEnum.STAFF );
        // service.save( c1 );
        assertEquals( s2.getUsername(), "staff2" );
        assertEquals( s2.getPassword(), "iamapassword" );

        final User s3 = new User( "staff2", "iamapassword!", UserEnum.STAFF );
        // service.save( c1 );
        assertEquals( s3.getUsername(), "staff2" );
        assertEquals( s3.getPassword(), "iamapassword!" );

        // testing creating a customer object invalid
        final Exception e2 = assertThrows( IllegalArgumentException.class, () -> c1.setPassword( null ) );
        assertEquals( "User needs a password.", e2.getMessage() );

    }

    /**
     * Tests user invalid username and passwords for both customer and staff
     */
    @Test
    @Transactional
    public void testInvalidLength () {
        // invalid customer password length
        try {
            new User( "customer1", "1", UserEnum.CUSTOMER );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid password length.", e.getMessage() );
        }
        try {
            new User( "customer1", "iamapasswordandiamgreat", UserEnum.CUSTOMER );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid password length.", e.getMessage() );
        }
        try {
            new User( "customer1", "iamapassword:", UserEnum.CUSTOMER );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid characters in password.", e.getMessage() );
        }
        // invalid customer username length
        try {
            new User( "user", "iamapassword", UserEnum.CUSTOMER );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid username length.", e.getMessage() );
        }
        try {
            new User( "customer!", "iamapassword", UserEnum.CUSTOMER );
            fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Incorrect IAE thrown", "Invalid characters in username.", e.getMessage() );
        }
    }

    /**
     * Tests user invalid username and passwords for both customer and staff
     */
    @Test
    @Transactional
    public void testInvalidLength2 () {

        final User s1 = new User( "customer1", "111111", UserEnum.STAFF );

        // invalid customer username length
        Exception e = assertThrows( IllegalArgumentException.class, () -> s1.setUsername( "c" ) );
        assertEquals( "Invalid username length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> s1.setUsername( "custo" ) );
        assertEquals( "Invalid username length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> s1.setUsername( "customer1111111111111111111" ) );
        assertEquals( "Invalid username length.", e.getMessage() );

        // invalid customer password length
        e = assertThrows( IllegalArgumentException.class, () -> s1.setPassword( "1" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> s1.setPassword( "11111" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
        e = assertThrows( IllegalArgumentException.class, () -> s1.setPassword( "111111111111111111111111" ) );
        assertEquals( "Invalid password length.", e.getMessage() );
    }

    /**
     * Tests user username and passwords for different characters
     */
    @Test
    @Transactional
    public void testCharacters () {

        // valid customer username
        final User c1 = new User( "customer1", "111111", UserEnum.CUSTOMER );

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
        final User c2 = new User( "customer2", "111111", UserEnum.CUSTOMER );
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

        // valid staff username
        final User s1 = new User( "staff1", "111111", UserEnum.STAFF );

        s1.setUsername( "staffffff" );
        assertEquals( s1.getUsername(), "staffffff" );
        s1.setUsername( "111111" );
        assertEquals( s1.getUsername(), "111111" );
        s1.setUsername( "STAFF1" );
        assertEquals( s1.getUsername(), "STAFF1" );
        s1.setUsername( "StAfF1" );
        assertEquals( s1.getUsername(), "StAfF1" );

        // valid staff password
        final User s2 = new User( "staff2", "111111", UserEnum.CUSTOMER );
        s2.setPassword( "cathyisawesome" );
        assertEquals( s2.getPassword(), "cathyisawesome" );
        s2.setPassword( "$un$un!" );
        assertEquals( s2.getPassword(), "$un$un!" );
        s2.setPassword( "LL@o@!" );
        assertEquals( s2.getPassword(), "LL@o@!" );

        // invalid staff password
        Exception e3 = assertThrows( IllegalArgumentException.class, () -> s2.setPassword( "^-^ *x* P_P" ) );
        assertEquals( "Invalid characters in password.", e3.getMessage() );
        e3 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "******" ) );
        assertEquals( "Invalid characters in password.", e3.getMessage() );
        e3 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( " o o o o o" ) );
        assertEquals( "Invalid characters in password.", e3.getMessage() );
        e3 = assertThrows( IllegalArgumentException.class, () -> c2.setPassword( "---------" ) );
        assertEquals( "Invalid characters in password.", e3.getMessage() );
    }

    /**
     * Tests if two objects are equal
     */
    @Test
    @Transactional
    public void testEquals () {
        final User c1 = new User( "customer1", "111111", UserEnum.CUSTOMER );
        final User c2 = new User( "customer1", "111111", UserEnum.CUSTOMER );
        // final User c3 = new User( "customer1", "555555" , UserEnum.CUSTOMER
        // );

        assertTrue( c1.equals( c2 ) );

    }

}
