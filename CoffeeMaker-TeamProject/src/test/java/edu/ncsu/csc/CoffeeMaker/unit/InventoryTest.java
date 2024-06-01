package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 500 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 500 ) );
        ivt.addIngredient( new Ingredient( "Milk", 500 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 500 ) );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getIngredient( "Chocolate" ).getAmount() );
        Assertions.assertEquals( 480, (int) i.getIngredient( "Milk" ).getAmount() );
        Assertions.assertEquals( 495, (int) i.getIngredient( "Sugar" ).getAmount() );
        Assertions.assertEquals( 499, (int) i.getIngredient( "Coffee" ).getAmount() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( new Ingredient( "Coffee", 5 ), new Ingredient( "Milk", 3 ), new Ingredient( "Sugar", 7 ), new Ingredient( "Chocolate", 2 ));

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredient( "Milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredient( "Sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredient( "Chocolate" ).getAmount( ),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( new Ingredient( "Coffee", -5 ), new Ingredient( "Milk", 3 ),
                    new Ingredient( "Sugar", 7 ), new Ingredient( "Chocolate", 2 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ).getAmount( ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ).getAmount( ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ).getAmount( ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( new Ingredient("Coffee", 5), new Ingredient("Milk", -3),
                    new Ingredient("Sugar", 7), new Ingredient("Chocolate", 2) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Milk").getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Sugar").getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Chocolate").getAmount(),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( new Ingredient("Coffee", 5), new Ingredient("Milk", 3),
                    new Ingredient("Sugar", -7), new Ingredient("Chocolate", 2));
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Milk").getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Sugar").getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Chocolate").getAmount(),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( new Ingredient("Coffee", 5), new Ingredient("Milk", 3),
                    new Ingredient("Sugar", 7), new Ingredient("Chocolate", -2));
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ).getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Milk").getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Sugar").getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient("Chocolate").getAmount(),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testCheckIngredientAmount () {
        final Inventory ivt = inventoryService.getInventory();

        // Test passing in a negative number
        assertThrows( IllegalArgumentException.class, () -> ivt.addIngredient( new Ingredient("Bread", - 1) ),
                "Amount cannot be negative" );

        // Test valid ingredient amounts
        assertDoesNotThrow( () -> ivt.addIngredient( new Ingredient("Bread", 1) ) );
        assertDoesNotThrow( () -> ivt.addIngredient( new Ingredient("Bread", 0) ) );
        assertDoesNotThrow( () -> ivt.addIngredient( new Ingredient("Bread", 123) ) );

    }

    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();

        assertEquals( "Chocolate: 500\n" + "Coffee: 500\n" + "Milk: 500\n" + "Sugar: 500\n", ivt.toString() );

        ivt.addIngredients( new Ingredient("Coffee", 5), new Ingredient("Milk", 3),
                new Ingredient("Sugar", 7), new Ingredient("Chocolate", 2));

        assertEquals( "Chocolate: 502\n" + "Coffee: 505\n" + "Milk: 503\n" + "Sugar: 507\n", ivt.toString() );

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient("Coffee", 505) );
        r.addIngredient( new Ingredient("Milk", 503) );
        r.addIngredient( new Ingredient("Sugar", 507) );
        r.addIngredient( new Ingredient("Chocolate", 502) );
        ivt.useIngredients( r );

        assertEquals( "Chocolate: 0\n" + "Coffee: 0\n" + "Milk: 0\n" + "Sugar: 0\n", ivt.toString() );
    }

    @Test
    @Transactional
    public void testEnoughIngredients () {

        final Inventory ivt = inventoryService.getInventory();

        // Create a recipe with the ingredient amounts equal to inventory
        final Recipe recipe = new Recipe();
        recipe.setName( "Full" );
        recipe.setPrice( 500 );
        recipe.addIngredient( new Ingredient( "Coffee", 500 ) );
        recipe.addIngredient( new Ingredient( "Milk", 500 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 500 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 500 ) );

        Assertions.assertTrue( ivt.enoughIngredients( recipe ) );

        // Test if the coffee is too high
        recipe.updateIngredient( "Coffee", 501 );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        recipe.updateIngredient( "Coffee", 500 );

        // Test if the milk is too high
        recipe.updateIngredient( "Milk", 501 );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        recipe.updateIngredient( "Milk", 500 );

        // Test if the sugar is too high
        recipe.updateIngredient( "Sugar", 501 );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        recipe.updateIngredient( "Sugar", 500 );

        // Test if the coffee is too high
        recipe.updateIngredient( "Chocolate", 501 );
        Assertions.assertFalse( ivt.enoughIngredients( recipe ) );
        recipe.updateIngredient( "Chocolate", 500 );

    }

}
