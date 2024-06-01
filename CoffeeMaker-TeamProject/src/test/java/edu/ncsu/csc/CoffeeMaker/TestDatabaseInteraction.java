package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    @Autowired
    private RecipeService recipeService;

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {

        // Create Mocha Recipe
        final Recipe r = new Recipe();

        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        recipeService.save( r );

        // Asset that the recipe exists in database
        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        final List<Ingredient> ingredients = r.getIngredients();

        assertEquals( ingredients, dbRecipe.getIngredients() );

        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );

        assertEquals( ingredients, dbRecipeByName.getIngredients() );

        final Integer newPrice = 15;
        final Integer newSugarCnt = 15;

        dbRecipe.setPrice( newPrice );

        dbRecipe.updateIngredient( "Sugar", newSugarCnt );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        final Recipe dbRecipeUpdated = recipeService.findAll().get( 0 );

        assertAll( "Grouped Assertions of Recipe", () -> assertEquals( r.getName(), dbRecipeUpdated.getName() ),
                () -> assertEquals( r.findByNameIngredient( "Chocolate" ),
                        dbRecipeUpdated.findByNameIngredient( "Chocolate" ) ),
                () -> assertEquals( r.findByNameIngredient( "Coffee" ),
                        dbRecipeUpdated.findByNameIngredient( "Coffee" ) ),
                () -> assertEquals( r.findByNameIngredient( "Milk" ), dbRecipeUpdated.findByNameIngredient( "Milk" ) ),
                () -> assertEquals( newPrice, dbRecipeUpdated.getPrice() ),
                () -> assertEquals( newSugarCnt, dbRecipeUpdated.findByNameIngredient( "Sugar" ).getAmount() ),
                () -> assertEquals( r.getClass(), dbRecipeUpdated.getClass() ) );
    }

    /**
     * Tests deleting recipe
     */
    @Test
    @Transactional
    public void testDelete () {
        // Create Mocha Recipe
        final Recipe r = new Recipe();

        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        recipeService.save( r );

        // Asset that the recipe exists in database
        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );

        recipeService.delete( r );

        final List<Recipe> dbRecipes1 = recipeService.findAll();
        assertEquals( 0, dbRecipes1.size() );

    }

}
