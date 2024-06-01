package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

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
    public void testCreateIngredientSuccess () throws Exception {
        final String name = "Coffee";
        final Integer amount = 5;
        final Ingredient ingredient = new Ingredient( name, amount );
        mvc.perform( post( "/api/v1/ingredients/" + name ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.name" ).value( name ) ).andExpect( jsonPath( "$.amount" ).value( amount ) );
    }

    @Test
    @Transactional
    public void testCreateIngredientFailure () throws Exception {
        final String name = "Coffee";
        final Integer amount = -1;
        final Ingredient ingredient = new Ingredient( name, amount );
        mvc.perform( post( "/api/v1/ingredients/" + name ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isBadRequest() );
    }

    @Test
    @Transactional
    public void testCreateIngredientIncorrectName () throws Exception {
        final String name = "Coffee";
        final Integer amount = 1;
        final Ingredient ingredient = new Ingredient( name, amount );
        mvc.perform( post( "/api/v1/ingredients/" + name + "e" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isBadRequest() );
    }

}
