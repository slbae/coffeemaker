package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * Customer for the coffee maker. Customer extends the User abstract class where
 * a User is defined. Customer is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support. A Customer can make orders, view their active orders,
 * and pickup orders.
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 *
 */
@Entity
public class Customer extends User {

    /**
     * Constructor for database.
     */
    public Customer () {

    }

    /**
     * A customer Constructor
     *
     * @param username
     *            the username for the customer
     * @param password
     *            the password for the customer
     */
    public Customer ( final String username, final String password ) {
        super( username, password, "Customer" );
    }

}
