package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests User database interactions, including login functionality
 *
 * @author Ellie Murphy
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APIUserTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /** context for mvc object */
    @Autowired
    private WebApplicationContext context;

    /** UserService object for testing */
    @Autowired
    private UserService           service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests createUser API call
     *
     * @throws Exception
     *             if exception thrown during testing
     */
    @Test
    @Transactional
    public void testCreateUser () throws Exception {
        service.deleteAll();

        final User customer = new User( "customer1", "thisisagoodpw", "Customer" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );
        assertEquals( 1, (int) service.count() );

        final User staff = new User( "staff1", "thisisagoodpw", "Staff" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( staff ) ) ).andExpect( status().isOk() );
        assertEquals( 2, (int) service.count() );

        final User duplicate = new User( "staff1", "thisisagoodpw", "Staff" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( duplicate ) ) ).andExpect( status().isConflict() );
    }

    /**
     * Tests get REST API call
     *
     * @throws Exception
     *             if exception thrown during testing
     */
    @Test
    @Transactional
    public void testGetUser () throws Exception {
        assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        assertEquals( null, service.findById( null ) );

        final User customer = new User( "customer1", "thisisagoodpw", "Customer" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );

        final String retVal = mvc.perform( get( String.format( "/api/v1/users/%s", "customer1" ) ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( retVal.contains( "customer1" ) );

        mvc.perform( get( String.format( "/api/v1/users/%s", "staff" ) ) ).andDo( print() )
                .andExpect( status().isNotFound() );
    }

    /**
     * Tests deleteUser API call
     *
     * @throws Exception
     *             if exception thrown during testing
     */
    @Test
    @Transactional
    public void testDeleteUser () throws Exception {
        service.deleteAll();

        final User customer = new User( "customer1", "thisisagoodpw", "Customer" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );

        final User staff = new User( "staff1", "thisisagoodpw", "Staff" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( staff ) ) ).andExpect( status().isOk() );
        assertEquals( 2, (int) service.count() );

        mvc.perform(
                delete( String.format( "/api/v1/users/%s", "customer1" ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        assertEquals( 1, service.findAll().size() );

        mvc.perform(
                delete( String.format( "/api/v1/users/%s", "customer1" ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
    }

    /**
     * Tests login API call
     *
     * @throws Exception
     *             if exception thrown during testing
     */
    @Test
    @Transactional
    public void testLogin () throws Exception {
        final User customer = new User( "customer1", "thisisagoodpw", "Customer" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );

        final User staff = new User( "staff1", "thisisagoodpw", "Staff" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( staff ) ) ).andExpect( status().isOk() );
        assertEquals( 2, (int) service.count() );

        mvc.perform( get( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( customer ) ) ).andExpect( status().isOk() );
    }

}
