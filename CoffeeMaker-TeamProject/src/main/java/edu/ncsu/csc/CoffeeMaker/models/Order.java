package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Order object that will be used whenever a customer wants to place an
 * order, or when a staff member wants to fulfill an order.
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 */
@Entity
@Table ( name = "orders" )
public class Order extends DomainObject {

    /** Order id */
    @Id
    @GeneratedValue
    private Long    id;

    /** The Recipe name associated with this order */
    private String  recipeName;

    /** The Customer id associated with this order */
    private Long    customerId;

    /**
     * This number will be used to track what number order this is for the day.
     * This will be mainly impacted by the frontend since they will increment
     * the number for each number
     */
    private Long    orderNumber;

    /**
     * A boolean to determine if the order has been fulfilled. Can be used later
     * when we want to log the number of orders completed
     */
    private boolean isFulfilled;

    /**
     * A boolean to determine if the order has been picked up. Can be used later
     * when we want to log the number of orders completed
     */
    private boolean isPickedUp;

    /**
     * Empty Constructor for Hibernate
     */
    public Order () {

    }

    /**
     * Constructs a Order object
     *
     * @param recipeName
     *            the name of the associated recipe
     * @param customerId
     *            the id of the associated customer
     * @param orderNum
     *            the order number
     */
    public Order ( final String recipeName, final Long customerId, final Long orderNum ) {
        this.recipeName = recipeName;
        this.customerId = customerId;
        this.orderNumber = orderNum;
        isFulfilled = false;
        isPickedUp = false;
    }

    /**
     * Get the ID of the Order
     *
     * @return the ID
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Sets the recipe name that the order is based on
     *
     * @param recipeName
     *            the recipe name
     */
    public void setRecipeName ( final String recipeName ) {
        this.recipeName = recipeName;
    }

    /**
     * Returns the Recipe Name
     *
     * @return recipeName a string
     */
    public String getRecipeName () {
        return recipeName;
    }

    /**
     * Returns the customer ID associated with the order
     *
     * @return customerId the customer ID
     */
    public Long getCustomerId () {
        return customerId;
    }

    /**
     * Sets the customer id
     *
     * @param customerId
     *            the customer ID
     */
    public void setCustomerId ( final Long customerId ) {
        this.customerId = customerId;
    }

    /**
     * This is called whenever the order is fulfilled. Updates the fulfilled
     * boolean to true
     */
    public void fulfillOrder () {
        isFulfilled = true;
    }

    /**
     * This is called whenever the order is picked up. Updates the isPickedUp
     * boolean to true
     */
    public void pickUpOrder () {
        isPickedUp = true;
    }

    /**
     * Shows if the order has been fulfilled
     *
     * @return true if the order has been fulfilled
     */
    public boolean isFulfilled () {
        return isFulfilled;
    }

    /**
     * Shows if the order has been pickedUp
     *
     * @return true if the order has been pickedUp
     */
    public boolean isPickedUp () {
        return isPickedUp;
    }

    /**
     * Returns the orderNumber for the order
     *
     * @return orderNumber the order number
     */
    public Long getOrderNumber () {
        return orderNumber;
    }

    /**
     * Sets the order number
     *
     * @param orderNumber
     *            the new associated order number for the order
     */
    public void setOrderNumber ( final Long orderNumber ) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString () {
        return "Order [orderId=" + id + ", recipeName=" + recipeName + ", customerId=" + customerId + ", fulfilled="
                + isFulfilled + "]";
    }

}
