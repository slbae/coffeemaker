package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handles creating an
 * ingredient.
 *
 * @author Caleb Twigg
 */
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  inventoryService;

    /**
     * REST API endpoint to add an ingredient
     *
     * @param name
     *            the name of the ingredient
     * @param ingredient
     *            the ingredient object (contains name and amount)
     * @return ingredient object with id if successful, otherwise BAD_REQUEST
     */
    @PostMapping ( BASE_PATH + "ingredients/{name}" )
    public ResponseEntity<Ingredient> createIngredient ( @PathVariable final String name,
            @RequestBody final Ingredient ingredient ) {
        try {
            if ( name == null || !name.equals( ingredient.getName() ) ) {
                throw new IllegalArgumentException( "Name in RequestParam must match name in request Body" );
            }
            final Inventory existingInventory = inventoryService.getInventory();
            final Ingredient existingIngredient = existingInventory.getIngredient( name );
            if ( existingIngredient != null ) {
                throw new IllegalArgumentException( "Ingredient already exists" );
            }
            ingredientService.save( ingredient );
            existingInventory.addIngredient( ingredient );
            inventoryService.save( existingInventory );
            return new ResponseEntity<Ingredient>( ingredient, HttpStatus.CREATED );
        }
        catch ( final Exception e ) {
            return new ResponseEntity<Ingredient>( HttpStatus.BAD_REQUEST );
        }
    }

}
