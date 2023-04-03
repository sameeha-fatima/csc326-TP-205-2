package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIUserController extends APIController {

    @Autowired
    private UserService userService;

    @GetMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity getUser ( @PathVariable ( "id" ) final String id ) {
        final User user = userService.findByName( id );
        return null == user
                ? new ResponseEntity( errorResponse( "No User found for username " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        if ( null != userService.findByName( user.getUsername() ) ) {
            return new ResponseEntity( errorResponse( "User with the id " + user.getUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        final User newUser = null;
        try {
            userService.save( user );
            return new ResponseEntity( newUser, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not create " + user.getUsername() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    public ResponseEntity login ( final User user ) {

    }

    @DeleteMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity deleteUser ( @PathVariable final String id ) {
        final User user = userService.findByName( id );
        try {
            if ( null == user ) {
                return new ResponseEntity( errorResponse( "No user found for id " + id ), HttpStatus.NOT_FOUND );
            }
            userService.delete( user );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Could not delete " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }
}
