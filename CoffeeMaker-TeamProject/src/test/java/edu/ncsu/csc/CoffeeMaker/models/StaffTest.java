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
 * Test program for Staff class.
 *
 * @author Sylbi Bae
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class StaffTest {

    /** Staff to test */
    private Staff       s1;

    /** Staff to test */
    private Staff       s2;

    @Autowired
    private UserService service;

    /** set up tests */
    @BeforeEach
    void beforeEach () {

        service.deleteAll();

        s1 = new Staff( "slb", "pass" );
        s2 = new Staff( "hoc", "pass" );

        service.save( s1 );
        service.save( s2 );
    }

    /** test creating invalid/valid Staff */
    @Test
    void testStaff () {
        // test valid Staff
        final Staff c = new Staff( "slbae", "password" );
        assertEquals( "slbae", c.getUsername() );
        assertEquals( "password", c.getPassword() );
        assertEquals( "Staff", c.getRole() );
        c.setUsername( "slb" );
        assertEquals( "slb", c.getUsername() );
        c.setPassword( "pw" );
        assertEquals( "pw", c.getPassword() );
        c.setRole( "Customer" );
        assertEquals( "Customer", c.getRole() );

        // test invalid Staff
        final Exception e1 = assertThrows( IllegalArgumentException.class, () -> new Staff( "invalid", "" ) );
        assertEquals( "Invalid password", e1.getMessage() );

        final Exception e2 = assertThrows( IllegalArgumentException.class, () -> new Staff( "", "password" ) );
        assertEquals( "Invalid username", e2.getMessage() );

        assertEquals( 2, service.count() );
    }

    /** test fulfillOrder() */
    @Test
    void testFulfillOrder () {
        // Customer orders coffee
        final Customer c1 = new Customer( "slbae", "pass" );
        final Order o1 = new Order();
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );
        // assertTrue( o1.addRecipe( r1 ) );
        // assertTrue( c1.orderCoffee( o1 ) );

        // Staff fulfills the order
        assertFalse( o1.isFulfilled() );
        assertTrue( s1.fulfillOrder( o1 ) );
        assertTrue( o1.isFulfilled() );

        assertFalse( s1.fulfillOrder( o1 ) );
    }

}
