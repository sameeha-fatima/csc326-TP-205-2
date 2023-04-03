package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class APIUserController extends APIController {

    @Autowired
    private UserService userService;

    @GetMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity getUser ( @PathVariable ( "id" ) final String id ) {

    }

    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final UserForm userF ) {

    }

    public ResponseEntity login ( final User user ) {

    }

    @DeleteMapping ( BASE_PATH + "/users/{id}" )
    public ResponseEntity deleteUser ( @PathVariable final String id ) {

    }
}
