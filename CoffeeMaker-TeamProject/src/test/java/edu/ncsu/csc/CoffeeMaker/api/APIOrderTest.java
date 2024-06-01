package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

/**
 * This will test the functionality of the APIOrderController class along with
 * the various behaviors of the Order class, and the OrderService and
 * OrderRepository
 *
 * @author Caleb Twigg
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderService          service;

    private Order                 o1;
    private Order                 o2;
    private Order                 o3;
    private Order                 o4;
    private Order                 o5;
    private Order                 o6;
    private Order                 o7;
    private Order                 o8;
    private Order                 o9;
    private Order                 o10;
    private Order                 o11;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * This will be used for later tests to add orders to the OrderService
     */
    private void addOrders () throws Exception {

        // Will be 10 total orders
        // With 5 active, and 5 fulfilled orders
        o1 = new Order( "First Recipe", (long) 1, (long) 1 );
        o2 = new Order( "Second Recipe", (long) 2, (long) 2 );
        o3 = new Order( "Third Recipe", (long) 3, (long) 3 );
        o4 = new Order( "Fourth Recipe", (long) 4, (long) 4 );
        o5 = new Order( "Fifth Recipe", (long) 5, (long) 5 );
        o6 = new Order( "First Recipe", (long) 1, (long) 6 );
        o7 = new Order( "Second Recipe", (long) 2, (long) 7 );
        o8 = new Order( "Third Recipe", (long) 3, (long) 8 );
        o9 = new Order( "Fourth Recipe", (long) 4, (long) 9 );
        o10 = new Order( "Fifth Recipe", (long) 5, (long) 10 );

        // Save these orders to the service using the API
        mvc.perform( post( "/api/v1/orders/all/" + o1.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o2.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o2 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o3.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o3 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o4.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o4 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o5.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o5 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o6.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o6 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o7.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o7 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o8.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o8 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o9.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o9 ) ) ).andExpect( status().isCreated() );
        mvc.perform( post( "/api/v1/orders/all/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isCreated() );

        // Create an object that will not be added to the service so it can
        // be used to prove that error responses work
        o11 = new Order( "Recipe", (long) 1, (long) 11 );

    }

    @Test
    @Transactional
    public void testCreateOrder () throws Exception {

        final Order nullOrder = null;
        final Order testOrder = new Order( "Test Recipe", (long) 1, (long) 1 );

        // Test adding the null Order which returns a 400 error from the higher
        // powers
        mvc.perform( post( "/api/v1/orders/all/" + 0 ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( nullOrder ) ) ).andExpect( status().isBadRequest() );

        // fail( TestUtils.asJsonString( testOrder ) );
        // Test adding the valid Order

        mvc.perform( post( "/api/v1/orders/all/" + testOrder.getOrderNumber() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( testOrder ) ) )
                .andExpect( status().isCreated() );

        assertEquals( 1, service.findAll().size() );

        assertNotNull( service.findOrder( testOrder ) );

        final Order savedOrder = service.findAll().get( 0 );

        final String expectedJSON = "{\"id\":" + savedOrder.getId() + ",\""
                + "recipeName\":\"Test Recipe\",\"customerId\":1,\"orderNumber\":1,\"isFulfilled\":false,\"isPickedUp\":false}";

        // Check if the object was uploaded as intended
        assertEquals( expectedJSON, TestUtils.asJsonString( savedOrder ) );
        // duplicate recipe rejected
        mvc.perform( post( "/api/v1/orders/all/" + testOrder.getOrderNumber() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( testOrder ) ) )
                .andExpect( status().is4xxClientError() );

        assertEquals( 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testGetOrder () throws Exception {
        // Add some orders for testing
        addOrders();

        // Test getting valid orders
        mvc.perform( get( "/api/v1/orders/all/" + o1.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o2.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o2 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o3.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o3 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o4.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o4 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o5.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o5 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o6.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o6 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o7.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o7 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o8.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o8 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o9.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o9 ) ) ).andExpect( status().isFound() );
        mvc.perform( get( "/api/v1/orders/all/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isFound() );

        // Test getting an invalid order that has not been added to the service
        mvc.perform( get( "/api/v1/orders/all/" + o11.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o11 ) ) ).andExpect( status().isNotFound() );

    }

    @Test
    @Transactional
    public void testFulfillOrder () throws Exception {

        addOrders();

        // Assign getFulfilledOrders() to a list
        List<Order> fulfilledOrders = service.getFulfilledOrders();
        // assertEquals( "", fulfilledOrders.toString() );
        assertEquals( 0, fulfilledOrders.size() );
        // Assign getActiveOrders() to a list
        List<Order> activeOrders = service.getActiveOrders();
        assertEquals( 10, activeOrders.size() );

        // Fulfill a valid order
        mvc.perform( put( "/api/v1/orders/all/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isOk() );

        // Fulfill an invalid order that has already been fulfilled
        mvc.perform( put( "/api/v1/orders/all/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isPreconditionFailed() );

        // Fulfill a order that has not been saved
        mvc.perform( put( "/api/v1/orders/all/" + o11.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o11 ) ) ).andExpect( status().isNotFound() );

        // Call getFulfilledOrders() to see if the order was added to the
        // fulfilled section
        fulfilledOrders = service.getFulfilledOrders();
        assertEquals( 1, fulfilledOrders.size() );

        // Call getActiveOrders() to see if the order is no longer in this list
        activeOrders = service.getActiveOrders();
        assertEquals( 9, activeOrders.size() );

    }

    @Test
    @Transactional
    public void testPickedUp () throws Exception {
        addOrders();

        // Assign getFulfilledOrders() to a list
        final List<Order> fulfilledOrders = service.getFulfilledOrders();
        // assertEquals( "", fulfilledOrders.toString() );
        assertEquals( 0, fulfilledOrders.size() );
        // Assign getActiveOrders() to a list
        final List<Order> activeOrders = service.getActiveOrders();
        assertEquals( 10, activeOrders.size() );

        mvc.perform( put( "/api/v1/orders/all/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON ) );

        mvc.perform( put( "/api/v1/orders/all/" + o11.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON ) );

        mvc.perform( put( "/api/v1/orders/fulfilled/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isOk() );

        // Fulfill an invalid order that has already been fulfilled
        mvc.perform( put( "/api/v1/orders/fulfilled/" + o10.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o10 ) ) ).andExpect( status().isPreconditionFailed() );

        // Fulfill a order that has not been saved
        mvc.perform( put( "/api/v1/orders/fulfilled/" + o11.getOrderNumber() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o11 ) ) ).andExpect( status().isNotFound() );
    }
}
