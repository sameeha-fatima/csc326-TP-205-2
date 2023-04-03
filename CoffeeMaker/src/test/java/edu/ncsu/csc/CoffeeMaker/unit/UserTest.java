package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.forms.UserForm;

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

    @Autowired
    // private UserService<User> service;

    private static final String USER1 = "user1";

    private static final String USER2 = "user2";

    private static final String USER3 = "user3";

    private static final String PW    = "abc123";

    // /**
    // * Set up
    // */
    // @BeforeEach
    // public void setup () {
    // service.deleteAll();
    // }

    /**
     *
     */
    public void testUser() {
        assertEquals(0, service.count());
        final UserForm newForm = new UserForm()
        User
    }

}
