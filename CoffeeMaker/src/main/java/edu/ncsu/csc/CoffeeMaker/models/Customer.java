package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

public class Customer extends User {

	public Customer(final String username, final String password) {
		super(username, password);
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
