package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Griffith Baker
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** ingredients in the inventory */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> currentInventory;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        this.currentInventory = new ArrayList<Ingredient>();
    }

    /**
     * Returns the current inventory
     * @return the current inventory
     */
    public List<Ingredient> getCurrentInventory () {
        return currentInventory;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the ingredient with the corresponding name
     * @param name the name of the ingredient
     * @return the ingredient with the corresponding name
     */
    public Ingredient findByIngredientName ( final String name ) {
        for ( final Ingredient ingredient : currentInventory ) {
            if ( ingredient.getName().equals( name ) ) {
                return ingredient;
            }
        }
        return null;
    }

    /**
     * Adds the ingredient to the current inventory
     * @param ingredient the ingredient to be added
     */
    public void addIngredient ( final Ingredient ingredient ) {
        if ( ingredient.getAmount() < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }
        boolean flag = false;

        for ( int i = 0; i < currentInventory.size(); i++ ) {
            if ( currentInventory.get( i ).getName().equals( ingredient.getName() ) ) {
                currentInventory.get( i ).setAmount( ingredient.getAmount() );
                flag = true;
                break;
            }
        }

        if ( !flag ) { // checking if updated the amount or add new ingredient
            currentInventory.add( ingredient );
        }

    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {

        final List<Ingredient> ingredientsInRecipe = r.getIngredients();
        // currentInventory

        for ( int i = 0; i < ingredientsInRecipe.size(); i++ ) {
            final Ingredient recipeIngredient = ingredientsInRecipe.get( i );

            final String recipeIngredientName = recipeIngredient.getName(); // get
                                                                            // the
                                                                            // name
            // of the
            // ingredient
            // currently
            // being looked
            // at in
            // inventory

            final Ingredient inventoryIngredient = this.findByIngredientName( recipeIngredientName );

            if ( inventoryIngredient == null ) {
                return false;
            }

            if ( inventoryIngredient.getAmount() < recipeIngredient.getAmount() ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {

        if ( enoughIngredients( r ) ) {
            final List<Ingredient> ingredientsInRecipe = r.getIngredients();

            for ( final Ingredient recipeIngredient : ingredientsInRecipe ) {
                final String recipeIngredientName = recipeIngredient.getName(); // get
                // the
                // matching
                // ingredients
                final Ingredient inventoryIngredient = this.findByIngredientName( recipeIngredientName );

                // subtract matching ingredient amount from current inventory
                inventoryIngredient.setAmount( inventoryIngredient.getAmount() - recipeIngredient.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Get the ingredient by the name
     *
     * @param name
     *            the name of the ingredient
     * @return Ingredient the ingredient object
     */
    public Ingredient getIngredient ( final String name ) {

        for ( final Ingredient ingredient : this.getCurrentInventory() ) {
            if ( ingredient.getName().equals( name ) ) {
                return ingredient;
            }
        }
        return null;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredients the ingredients to add
     *
     * @return boolean true if successful, false otherwise
     *
     */

    public boolean addIngredients ( final Ingredient... ingredients ) { // variable
        // number of
        // parameters
        final List<Ingredient> list = Arrays.asList( ingredients );

        for ( final Ingredient ingredient : list ) { // error check
            if ( ingredient.getAmount() < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
        }

        for ( final Ingredient ingredient : list ) {

            final String name = ingredient.getName();
            int index = -1; // error if still -1
            for ( int j = 0; j < currentInventory.size(); j++ ) {
                // finds
                // the
                // index
                // of the
                // ingredient
                // from
                // recipe
                // in
                // inventory
                final String nameInInventory = currentInventory.get( j ).getName();
                if ( nameInInventory.equals( name ) ) {
                    index = j;
                    break;
                }
            }
            if ( index == -1 ) { // if ingredient not in inventory, add it
                currentInventory.add( ingredient );
            }
            else {
                currentInventory.get( index )
                        .setAmount( currentInventory.get( index ).getAmount() + ingredient.getAmount() );
            }
        }

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        final List<Ingredient> list = getCurrentInventory();
        for ( final Ingredient ingredient : list ) {
            buf.append( ingredient.getName() );
            buf.append( ": " );
            buf.append( ingredient.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
