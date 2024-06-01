package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * The OrderService is used to handle CRUD operations on the Order model. In
 * addition to all functionality in `Service`, we also manage the Order
 * singleton.
 *
 * @author Caleb Twigg
 *
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    /**
     * OrderRepository, to be autowired in by Spring and provide CRUD operations
     * on Order model.
     */
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    @Override
    public String toString () {
        return "OrderService{" + "orderRepository=" + orderRepository + '}';
    }

    /**
     * When passed an Order, this method will look for an order with the same
     * order number uses the findByOrderNum class
     *
     * @param order
     *            the Order passed to the method
     * @return null if the Order has not beeen saved
     */
    public Object findOrder ( final Order order ) {

        return findByOrderNum( order.getOrderNumber() );
    }

    /**
     * Using the order Number from an associated Order, look to see if there is
     * another order with the same number that has been saved
     *
     * @param orderNum
     *            the Order num we have been given to check for
     * @return null if there is not an order with that OrderNum saved
     */
    public Order findByOrderNum ( final Long orderNum ) {

        final List<Order> orderList = orderRepository.findAll();

        Order returnOrder = null;

        for ( int i = 0; i < orderList.size(); i++ ) {
            if ( orderNum == orderList.get( i ).getOrderNumber() ) {
                returnOrder = orderList.get( i );
            }
        }

        return returnOrder;
    }

    /**
     * Returns all the fulfilled orders in a list format
     *
     * @return orderList the list of fulfilled orders
     */
    public List<Order> getFulfilledOrders () {

        final List<Order> orderList = findAll();

        // Remove all active orders from the list
        for ( int i = orderList.size() - 1; i >= 0; i-- ) {

            if ( !orderList.get( i ).isFulfilled() || orderList.get( i ).isPickedUp() ) {
                orderList.remove( i );
            }

        }

        return orderList;
    }

    /**
     * Returns all the active orders in a list format
     *
     * @return orderList the list of all active orders
     */
    public List<Order> getActiveOrders () {

        final List<Order> orderList = findAll();

        // Remove all fulfilled orders from the list
        for ( int i = orderList.size() - 1; i >= 0; i-- ) {

            if ( orderList.get( i ).isFulfilled() ) {
                orderList.remove( i );
            }

        }

        return orderList;
    }

}
