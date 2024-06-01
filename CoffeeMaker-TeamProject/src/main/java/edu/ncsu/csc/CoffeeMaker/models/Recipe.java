package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** Ingredients in the recipe */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Returns an ingredient based on the name
     *
     * @param name
     *            name of the ingredient
     * @return the ingredient with the name or null
     */
    public Ingredient findByNameIngredient ( final String name ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) ) {
                return ingredients.get( i );
            }
        }
        return null;
    }

    /**
     * Update an ingredient based on the name
     *
     * @param name
     *            name of the ingredient
     * @param amount
     *            the amount to set the ingredient to
     */
    public void updateIngredient ( final String name, final int amount ) {
        final Ingredient ingredient = this.findByNameIngredient( name );
        if ( ingredient == null ) {
            throw new NoSuchElementException( "The ingredient with that name was not found" );
        }
        ingredient.setAmount( amount );

    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param ingredient
     *            The ingredient to add.
     */
    public void addIngredient ( final Ingredient ingredient ) {
        this.ingredients.add( ingredient );
    }

    /**
     * Updates the current recipe to a recipe passed as a parameter
     *
     * @param newRecipe
     *            the new recipe to copy over
     */
    public void updateRecipe ( final Recipe newRecipe ) {
        setPrice( newRecipe.getPrice() );

        // Clears the list of ingredients in the recipe
        ingredients.removeAll( ingredients );

        final List<Ingredient> newIngredients = newRecipe.getIngredients();

        // Adds in the new list of ingredients
        ingredients.addAll( newIngredients );
    }

    /**
     * Returns the list of ingredients
     *
     * @return ingredients the list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the name, price, and ingredients in the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {

        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
