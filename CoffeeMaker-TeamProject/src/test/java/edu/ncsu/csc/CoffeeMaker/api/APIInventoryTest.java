package edu.ncsu.csc.CoffeeMaker.api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
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
public class APIInventoryTest {

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
    public void testSetAndGetInventory() throws Exception {

        Inventory ivt = new Inventory();
        ivt.addIngredients(new Ingredient("Coffee", 5),
                new Ingredient("Milk", 5),
                new Ingredient("Sugar", 5),
                new Ingredient("Chocolate", 5)  );


        mvc.perform( put("/api/v1/inventory").contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString(ivt))
        );

        mvc.perform( get( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON ) ).andDo( print() ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.currentInventory[0].name" ).value( "Coffee" ) )
                .andExpect( jsonPath( "$.currentInventory[0].amount" ).value( 5 ) )
                .andExpect( jsonPath( "$.currentInventory[1].name" ).value( "Milk" ) )
                .andExpect( jsonPath( "$.currentInventory[1].amount" ).value( 5 ) )
                .andExpect( jsonPath( "$.currentInventory[2].name" ).value( "Sugar" ) )
                .andExpect( jsonPath( "$.currentInventory[2].amount" ).value( 5 ) )
                .andExpect( jsonPath( "$.currentInventory[3].name" ).value( "Chocolate" ) )
                .andExpect( jsonPath( "$.currentInventory[3].amount" ).value( 5 ) );


    }

}
