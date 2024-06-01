package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredients Object
 *
 * @author Caleb Twigg
 * @author Griffith Baker
 * @author Nick Case
 */
@Entity
public class Ingredient extends DomainObject {

    /** id used for the database */
    @Id
    @GeneratedValue
    private Long    id;

    /** name of the ingredient */
    private String  name;

    /** amount of the ingredient */
    @Min ( 0 )
    private Integer amount;

    /**
     * Empty constructor for Hibernate
     */
    public Ingredient () {

    }

    /**
     * Constructor for Ingredient
     *
     * @param ingredient
     *            name of the ingredient
     * @param amount
     *            amount of the ingredient
     */
    public Ingredient ( final String ingredient, final Integer amount ) {

        this.name = ingredient;
        this.amount = amount;
    }

    private void checkAmount ( final Integer amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }
    }

    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the amount of the ingredient
     * @return the amount of the ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient
     * @param amount the amount of the ingredient
     */
    public void setAmount ( final Integer amount ) {
        this.checkAmount( amount );
        this.amount = amount;
    }

    /**
     * Returns the ingredient name
     * @return the ingredient name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the ingredient name
     * @param name the ingredient name
     */
    public void setName ( String name ) {
        this.name = name;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

    @Override
    public Serializable getId () {
        return id;
    }

}
