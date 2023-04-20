package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;

/**
 * Tests class for APICustomerOrder
 *
 * @author cbdocke2
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APICustomerOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /** context for mvc object */
    @Autowired
    private WebApplicationContext context;

    /** Recipe service object */
    @Autowired
    private CustomerOrderService  service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests adding Orders
     *
     * @throws Exception
     *             if something is wrong
     */
    @Test
    @Transactional
    public void testAddOrder () throws Exception {

        assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        final Ingredient ingredient1 = new Ingredient( "Ingredient1", 12 );
        ingredients1.add( ingredient1 );
        final Recipe recipe1 = new Recipe( ingredients1, 12 );
        recipes.add( recipe1 );
        final CustomerOrder order1 = new CustomerOrder( "Order1", "Customer", recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order1 ) ) ).andExpect( status().isOk() );
        assertEquals( 1, service.findAll().size(), "There should be one Order in CoffeeMaker" );

        assertEquals( order1, service.findByName( "Order1" ) );

        final List<Recipe> recipes2 = new ArrayList<Recipe>();
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        final Ingredient ingredient2 = new Ingredient( "Ingredient2", 12 );
        ingredients1.add( ingredient2 );
        final Recipe recipe2 = new Recipe( ingredients2, 12 );
        recipes.add( recipe2 );
        final CustomerOrder order2 = new CustomerOrder( "Order2", "Customer", recipes2 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order2 ) ) ).andExpect( status().isOk() );
        assertEquals( 2, service.findAll().size(), "There should be one Order in CoffeeMaker" );

    }

    /**
     * Tests editing Orders
     *
     * @throws Exception
     *             if something is wrong
     */
    @Test
    @Transactional
    public void testEditOrder () throws Exception {

        assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        final Ingredient ingredient1 = new Ingredient( "Ingredient1", 12 );
        ingredients1.add( ingredient1 );
        final Recipe recipe1 = new Recipe( ingredients1, 12 );
        recipes.add( recipe1 );
        final CustomerOrder order3 = new CustomerOrder( "Order3", "Customer", recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order3 ) ) ).andExpect( status().isOk() );
        assertEquals( 1, service.findAll().size(), "There should be one Order in CoffeeMaker" );

        assertEquals( order3, service.findByName( "Order3" ) );

        mvc.perform( put( String.format( "/api/v1/orders" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order3 ) ) ).andExpect( status().isOk() );

        assertEquals( service.findByName( "Order3" ).isFulfilled(), true );

    }

    /**
     * Tests getting Orders
     *
     * @throws Exception
     *             if something is wrong
     */
    @Test
    @Transactional
    public void testGetOrder () throws Exception {

        assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final List<Recipe> recipes = new ArrayList<Recipe>();
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        final Ingredient ingredient1 = new Ingredient( "Ingredient1", 12 );
        ingredients1.add( ingredient1 );
        final Recipe recipe1 = new Recipe( ingredients1, 12 );
        recipes.add( recipe1 );
        final CustomerOrder order1 = new CustomerOrder( "Order1", "Customer", recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order1 ) ) ).andExpect( status().isOk() );
        assertEquals( 1, service.findAll().size(), "There should be one Order in CoffeeMaker" );

        assertEquals( order1, service.findByName( "Order1" ) );

        final List<Recipe> recipes2 = new ArrayList<Recipe>();
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        final Ingredient ingredient2 = new Ingredient( "Ingredient2", 12 );
        ingredients1.add( ingredient2 );
        final Recipe recipe2 = new Recipe( ingredients2, 12 );
        recipes.add( recipe2 );
        final CustomerOrder order2 = new CustomerOrder( "Order2", "Customer", recipes2 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order2 ) ) ).andExpect( status().isOk() );
        assertEquals( 2, service.findAll().size(), "There should be two Orders in CoffeeMaker" );

        final String orderstring1 = mvc.perform( get( String.format( "/api/v1/orders/%s", "Order1" ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( orderstring1.contains( "Order1" ) );
        final String orderstring2 = mvc.perform( get( String.format( "/api/v1/orders/%s", "Order2" ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertTrue( orderstring2.contains( "Order2" ) );

        // no order found with this name
        mvc.perform( get( String.format( "/api/v1/orders/%s", "Order12No" ) ) )
                .andExpect( status().is4xxClientError() );

    }

}
