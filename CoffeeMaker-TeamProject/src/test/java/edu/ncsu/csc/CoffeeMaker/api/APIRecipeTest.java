package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "Coffee", 3 ) );
        r.addIngredient( new Ingredient( "Sugar", 8 ) );
        r.addIngredient( new Ingredient( "Milk", 4 ) );
        r.addIngredient( new Ingredient( "Chocolate", 5 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    @Test
    @Transactional
    public void testEditRecipe () throws Exception {

        // First we test copies
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r1Copy = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( "Coffee", 51, 4, 2, 2, 1 );
        final Recipe r3 = createRecipe( "Mocha", 40, 3, 3, 3, 3 );

        service.save( r1 );

        final String copyResponse = mvc
                .perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r1Copy ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( "{\"status\":\"success\",\"message\":\"Coffee was edited successfully\"}", copyResponse );

        // Second we test actual edited recipes
        final String editedResponse = mvc
                .perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( "{\"status\":\"success\",\"message\":\"Coffee was edited successfully\"}", editedResponse );

        // Finally test editing a recipe that never was saved
        final String errorResponse = mvc
                .perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( r3 ) ) )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        assertEquals( "{\"status\":\"failed\",\"message\":\"No recipe found for name Mocha\"}", errorResponse );

    }

    @Test
    @Transactional
    public void DeleteRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */ // create the recipe
                                                          // to delete w/ API

        final String recipeName = "Coffee";

        if ( !recipe.contains( recipeName ) ) {
            final Recipe r = new Recipe();
            r.setName( recipeName );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Sugar", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 5 ) );
            r.addIngredient( new Ingredient( "Chocolate", 5 ) );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

            recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                    .getResponse().getContentAsString();

            assertTrue( recipe.contains( recipeName ) );

            final String deleteStatusMessage = mvc
                    .perform( delete( String.format( "/api/v1/recipes/%s", recipeName ) ) ).andDo( print() )
                    .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

            assertEquals( "{\"status\":\"success\",\"message\":\"Coffee was deleted successfully\"}",
                    deleteStatusMessage );

            final String recipesGetBody = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                    .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

            assertEquals( "[]", recipesGetBody );
        }

    }

    @Test
    @Transactional
    public void testUpdateRecipe () throws Exception {
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */ // create the recipe
        // to delete w/ API

        final String recipeName = "Coffee";

        if ( !recipe.contains( recipeName ) ) {
            final Recipe r = new Recipe();
            r.setName( recipeName );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Sugar", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 5 ) );
            r.addIngredient( new Ingredient( "Chocolate", 5 ) );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }

        final Recipe r = new Recipe();
        r.setName( recipeName + "2" );
        r.addIngredient( new Ingredient( "Coffee", 50 ) );
        r.addIngredient( new Ingredient( "Sugar", 50 ) );
        r.addIngredient( new Ingredient( "Milk", 50 ) );
        r.addIngredient( new Ingredient( "Chocolate", 50 ) );

        mvc.perform( post( String.format( "/api/v1/recipes/%s", recipeName ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        r.setPrice( 5 );
        mvc.perform( post( "/api/v1/recipes" + r.getName() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isNotFound() );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient( "Coffee", coffee ) );
        recipe.addIngredient( new Ingredient( "Sugar", sugar ) );
        recipe.addIngredient( new Ingredient( "Milk", milk ) );
        recipe.addIngredient( new Ingredient( "Chocolate", chocolate ) );

        return recipe;
    }

}
