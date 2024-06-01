package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * Staff for the coffee maker. Staff extends the User abstract class where a
 * User is defined. Staff is tied to the database using Hibernate libraries. See
 * UserRepository and UserService for the other two pieces used for database
 * support. A Staff user can perform Recipe and Inventory functions, and fulfill
 * Customer orders.
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 *
 */
@Entity
public class Staff extends User {

    /**
     * Constructor for database.
     */
    public Staff () {

    }

    /**
     * Creates a Staff user.
     *
     * @param username
     *            the username of the staff
     * @param password
     *            the password of the staff
     */
    public Staff ( final String username, final String password ) {
        super( username, password, "Staff" );
    }

    /**
     * Updates the given Order to be fulfilled.
     *
     * @param order
     *            to fulfill
     * @return true if order was successfully fulfilled
     */
    public boolean fulfillOrder ( final Order order ) {
        if ( order != null && !order.isFulfilled() ) { // if Order is not null
                                                       // and not already made
                                                       // --> fulfill order
            order.fulfillOrder();
            return true;
        }
        return false;
    }

    // /**
    // * Deletes the given Recipe from the recipe menu by delegating to ______.
    // * @param recipe to delete
    // * @return true if recipe was successfully deleted
    // */
    // public boolean deleteRecipe(Recipe recipe) {
    // // performed by APIRecipeController
    // return false;
    //
    // }
    //
    // /**
    // * Updates the given Recipe by delegating to Recipe.updateRecipe.
    // * @param recipe to new Recipe to update the existing Recipe
    // * @return true if recipe was successfully updated
    // */
    // public boolean updateRecipe(Recipe recipe) {
    // if (recipe != null) {
    // recipe.updateRecipe(recipe);
    // return true;
    // }
    // return false;
    // }

}
