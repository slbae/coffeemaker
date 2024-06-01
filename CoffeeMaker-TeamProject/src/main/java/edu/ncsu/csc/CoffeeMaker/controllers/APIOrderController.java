package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
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
     * REST API method to provide GET access to a specific order, as indicated
     * by the path variable provided (the id of the order desired)
     *
     * @param orderNum
     *            order name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/all/{orderNum}" )
    public ResponseEntity getOrder ( @PathVariable ( "orderNum" ) final Long orderNum ) {
        final Order order = service.findByOrderNum( orderNum );
        return null == order
                ? new ResponseEntity( errorResponse( "The Order #" + orderNum + "does not Exist" ),
                        HttpStatus.NOT_FOUND )
                : new ResponseEntity( order, HttpStatus.FOUND );
    }

    /**
     * REST API method to provide POST access to the Order model. This is used
     * to create a new Order by automatically converting the JSON RequestBody
     * provided to a Order object. Invalid JSON will fail.
     *
     * @param order
     *            The valid Order to be saved.
     * @return ResponseEntity indicating success if the Order could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders/all/{orderNumber}" )
    public ResponseEntity createOrder ( @RequestBody final Order order ) {
        if ( service.findOrder( order ) != null ) {
            return new ResponseEntity( errorResponse( "Order #" + order.getOrderNumber() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        else {
            service.save( order );
            return new ResponseEntity( successResponse( "Order #" + order.getOrderNumber() + "successfully created" ),
                    HttpStatus.CREATED );
        }

    }

    /**
     * REST API method to allow deleting a Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * order to update as fulfilled
     *
     * @param orderNum
     *            The name of the Order to delete
     * @return Success if the order could be deleted; an error if the order does
     *         not exist
     */
    @PutMapping ( BASE_PATH + "/orders/all/{orderNum}" )
    public ResponseEntity fulfillOrder ( @PathVariable final Long orderNum ) {

        final Order order = service.findByOrderNum( orderNum );

        // If the order doesnt exist, return a 404 Error
        if ( order == null ) {
            return new ResponseEntity( errorResponse( "Order #" + orderNum + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        // If the order has already been fulfilled, return a 412 Error
        else if ( order.isFulfilled() ) {
            return new ResponseEntity( errorResponse( "Order #" + orderNum + " has already been fulfilled" ),
                    HttpStatus.PRECONDITION_FAILED );
        }
        // The order is active and can be fulfilled and return a 200 OK
        else {
            // Fulfill the order
            order.fulfillOrder();
            // Update the service with the fulfilled order
            service.save( order );
            return new ResponseEntity( successResponse( "Order #" + orderNum + " has been fulfilled" ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to allow deleting a Order from the CoffeeMaker's
     * Inventory, by making a PUT request to the API endpoint and indicating the
     * order to update as fulfilled
     *
     * @param orderNum
     *            The name of the Order to delete
     * @return Success if the order could be deleted; an error if the order does
     *         not exist
     */
    @PutMapping ( BASE_PATH + "/orders/fulfilled/{orderNum}" )
    public ResponseEntity pickedUp ( @PathVariable final Long orderNum ) {

        final Order order = service.findByOrderNum( orderNum );

        // If the order doesnt exist, return a 404 Error
        if ( order == null ) {
            return new ResponseEntity( errorResponse( "Order #" + orderNum + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        // If the order has already been fulfilled, return a 412 Error
        else if ( order.isPickedUp() ) {
            return new ResponseEntity( errorResponse( "Order #" + orderNum + " has already been picked up" ),
                    HttpStatus.PRECONDITION_FAILED );
        }
        // The order is active and can be fulfilled and return a 200 OK
        else {
            // Fulfill the order
            order.pickUpOrder();
            // Update the service with the fulfilled order
            service.save( order );
            return new ResponseEntity( successResponse( "Order #" + orderNum + " has been picked up" ), HttpStatus.OK );
        }

    }

    /**
     * REST API method to provide GET access to all orders in the system
     *
     * @return JSON representation of all orders
     */
    @GetMapping ( BASE_PATH + "/orders/all" )
    public List<Order> getOrders () {
        return service.findAll();
    }

    /**
     * Retrieves all the Active orders that are in the system
     *
     * @return orderList the list of Orders that are currently active and
     *         waiting to be fulfilled
     */
    @GetMapping ( BASE_PATH + "/orders/active" )
    public List<Order> getActiveOrders () {

        return service.getActiveOrders();
    }

    /**
     * Retrieves all the fulfilled Orders that are currently in the system
     *
     * @return orderList the list of all the orders that have been fulfilled
     */
    @GetMapping ( BASE_PATH + "/orders/fulfilled" )
    public List<Order> getFulfilledOrders () {

        return service.getFulfilledOrders();
    }

    /**
     * Retrieves all the fulfilled Orders that are for a specific user
     *
     * @param userid
     *            The current User's id number which will be used to find
     *            specific orders associated with that id
     *
     * @return returnList the list of all the of the users orders that have been
     *         fulfilled
     */
    @GetMapping ( BASE_PATH + "/orders/fulfilled/{userid}" )
    public List<Order> getMyCurrentOrders ( @PathVariable final Long userid ) {

        final List<Order> returnList = service.getFulfilledOrders();

        for ( int i = 0; i < returnList.size(); i++ ) {
            if ( returnList.get( i ).getCustomerId() != userid ) {
                returnList.remove( i );
            }
        }

        return returnList;
    }

}
