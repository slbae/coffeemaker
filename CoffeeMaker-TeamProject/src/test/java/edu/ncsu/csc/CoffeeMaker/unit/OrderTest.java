package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Test
    @Transactional
    public void testOrderSettersGetters () {

        final Order o1 = new Order();
        final String testRecipe = "Test Recipe";
        final Long cid = (long) 1;
        final Long oid = (long) 2;

        assertNull( o1.getRecipeName() );
        assertNull( o1.getCustomerId() );
        assertNull( o1.getOrderNumber() );

        o1.setRecipeName( testRecipe );
        o1.setCustomerId( cid );
        o1.setOrderNumber( oid );

        assertEquals( testRecipe, o1.getRecipeName() );
        assertEquals( cid, o1.getCustomerId() );
        assertEquals( oid, o1.getOrderNumber() );

    }

    @Test
    @Transactional
    public void testFulfill () {

        final Order o1 = new Order( "One Recipe", (long) 1, (long) 1 );
        assertFalse( o1.isFulfilled() );

        o1.fulfillOrder();
        assertTrue( o1.isFulfilled() );
    }

    @Test
    @Transactional
    public void testOrderService () {

        List<Order> orderList = orderService.findAll();

        assertEquals( 0, orderList.size() );

        final Order o1 = new Order( "One Recipe", (long) 1, (long) 1 );
        final Order o2 = new Order( "Two Recipe", (long) 2, (long) 2 );
        final Order o3 = new Order( "Three Recipe", (long) 3, (long) 3 );

        orderService.save( o1 );
        orderService.save( o2 );
        orderService.save( o3 );

        orderList = orderService.findAll();

        assertEquals( 3, orderList.size() );

        // Verify that the objects are being saved as intended
        assertEquals( o1.getId(), orderList.get( 0 ).getId() );
        assertEquals( o1.getRecipeName(), orderList.get( 0 ).getRecipeName() );
        assertEquals( o1.getCustomerId(), orderList.get( 0 ).getCustomerId() );
    }

    @Test
    @Transactional
    public void testToString () {

        final Order o1 = new Order( "One Recipe", (long) 1, (long) 1 );

        final String orderString = "Order [orderId=null, recipeName=One Recipe, customerId=1, fulfilled=false]";

        assertEquals( orderString, o1.toString() );

    }

}
