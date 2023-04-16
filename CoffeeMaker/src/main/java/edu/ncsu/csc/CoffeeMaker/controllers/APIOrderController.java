package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 *
 * @author cbdocke2
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the Order model
     */
    @Autowired
    private OrderService service;

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @return JSON representation of all recipes
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<Order> getOrders () {
        return service.findAll();
    }

    /**
     * REST API method to provide POST access to the Order model. This is used
     * to create a new Order by automatically converting the JSON RequestBody
     * provided to a Order object. Invalid JSON will fail.
     *
     * @param order
     *            The valid Order to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final Order order ) {
        if ( null != service.findByName( order.getCustomerUsername() ) ) {
            return new ResponseEntity(
                    errorResponse( "Order with the name " + order.getCustomerUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        service.save( order );
        return new ResponseEntity( successResponse( order.getCustomerUsername() + " successfully created" ),
                HttpStatus.OK );

    }

    /**
     * REST API method to provide PUT access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired), with an
     * updated price and list of ingredients
     *
     * @param o
     *            represents o object with updated price and ingredients should
     *            have
     * @param inv
     *            Inventory that will be looked at to make sure the recipes can
     *            be fulfilled with the amount of ingredients in Inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/orders" )
    public ResponseEntity editOrder ( @RequestBody final Order o, final Inventory inv ) {
        if ( o == null || o.getName() == null ) {
            return new ResponseEntity( errorResponse( "Order name is null" ), HttpStatus.BAD_REQUEST );
        }
        final Order order = service.findByName( o.getName() );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found with name " + o.getCustomerUsername() ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            order.fulfillOrder( inv );
            service.save( order );
        }
        catch ( final IllegalArgumentException e ) {
            return new ResponseEntity( errorResponse( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
        return new ResponseEntity( successResponse( order.getCustomerUsername() + " successfully updated" ),
                HttpStatus.OK );
    }

}
