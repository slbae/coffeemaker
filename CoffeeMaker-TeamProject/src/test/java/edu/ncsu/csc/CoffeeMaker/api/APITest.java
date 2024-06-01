package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * Tests the API for the CoffeeMaker project
 *
 * @author Caleb Twigg
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void ensureRecipeExists () throws Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "Equal Coffee" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient( "Chocolate", 5 ) );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Sugar", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 5 ) );
            r.setPrice( 5 );
            r.setName( "Equal Coffee" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe.contains( "Equal Coffee" ) );

        final Inventory i = new Inventory();
        i.addIngredients( new Ingredient( "Chocolate", 50 ), new Ingredient( "Coffee", 50 ),
                new Ingredient( "Sugar", 50 ), new Ingredient( "Milk", 50 ) );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Equal Coffee" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 100 ) ) )
                .andExpect( status().isOk() ).andDo( print() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Equal Coffee" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 100 ) ) )
                .andExpect( status().isOk() ).andDo( print() );
    }

}
