package edu.ncsu.csc.CoffeeMaker.models;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Test program for Customer class.
 *
 * @author Sylbi Bae
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class CustomerTest {

    /** Customer to test */
    private Customer    c1;

    /** Customer to test */
    private Customer    c2;

    @Autowired
    private UserService userService;

    /**
     * Set up tests
     */
    @BeforeEach
    void setUp () {
        c1 = new Customer( "slbae", "pass" );
        c2 = new Customer( "hsoconne", "pass" );
    }

    /** test creating invalid/valid Customers */
    @Test
    void testCustomer () {
        // test valid Customer
        final Customer c = new Customer( "slbae", "password" );
        assertEquals( "slbae", c.getUsername() );
        assertEquals( "password", c.getPassword() );
        assertEquals( "Customer", c.getRole() );
        c.setUsername( "slb" );
        assertEquals( "slb", c.getUsername() );
        c.setPassword( "pw" );
        assertEquals( "pw", c.getPassword() );
        c.setRole( "Staff" );
        assertEquals( "Staff", c.getRole() );

        // test invalid Customers
        final Exception e1 = assertThrows( IllegalArgumentException.class, () -> new Customer( "invalid", "" ) );
        assertEquals( "Invalid password", e1.getMessage() );

        final Exception e2 = assertThrows( IllegalArgumentException.class, () -> new Customer( "", "password" ) );
        assertEquals( "Invalid username", e2.getMessage() );
    }

    /** test orderCoffee() & pickupCoffee() */
    @Test
    void testOrderAndPickupCoffee () {
        // Create Orders
        final Order o1 = new Order();
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );
        // assertTrue( o1.addRecipe( r1 ) );

        final Order o2 = new Order();
        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        // assertTrue( o2.addRecipe( r2 ) );

        final Recipe r3 = new Recipe();
        r3.setName( "Tasty Drink" );
        r3.setPrice( 12 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 3 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 3 ) );
        // assertTrue( o2.addRecipe( r3 ) );

        /*
         * // Order and test history and activeOrders lists assertTrue(
         * c1.orderCoffee( o1 ) ); final List<Order> h1 = c1.getHistory(); final
         * List<Order> a1 = c1.getActiveOrders(); assertEquals( h1, a1 );
         * assertEquals( 1, h1.size() ); assertEquals( r1, h1.get( 0 ) );
         * assertTrue( c1.orderCoffee( o2 ) ); final List<Order> h2 =
         * c2.getHistory(); final List<Order> a2 = c2.getActiveOrders();
         * assertEquals( h2, a2 ); assertEquals( 2, h2.size() ); assertEquals(
         * r2, h2.get( 0 ) ); assertEquals( r3, h2.get( 1 ) ); // pick up order
         * 1 // assertTrue( c1.pickupCoffee( o1 ) ); final List<Order>
         * activeOrders = c1.getActiveOrders(); assertEquals( 1,
         * activeOrders.size() ); assertEquals( o2, activeOrders.get( 0 ) ); //
         * pick up order 2 // assertTrue( c1.pickupCoffee( o2 ) ); final
         * List<Order> activeOrders2 = c1.getActiveOrders(); assertEquals( 0,
         * activeOrders2.size() );
         */

    }

    /** test equals() & toString() */
    @Test
    void testOther () {
        assertFalse( c1.toString().equals( c2.toString() ) );
        assertTrue( c1.toString().equals( c1.toString() ) );

        assertFalse( c1.equals( c2 ) );
        assertTrue( c1.equals( c1 ) );
    }

}
