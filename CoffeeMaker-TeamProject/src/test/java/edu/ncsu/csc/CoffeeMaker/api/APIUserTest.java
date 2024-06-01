package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIUserTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService           service;

    private Customer              c1;
    private Staff                 s1;

    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();

        c1 = new Customer( "user", "pass" );
        s1 = new Staff( "staff", "eww" );
    }

    /**
     * Tests adding a user
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void createUser () throws Exception {
        service.deleteAll();
        // Tests a successful add
        c1 = new Customer( "user", "pass" );

        assertEquals( 0, (int) service.count() );

        mvc.perform( post( "/api/v1/users/" + c1.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) );

        // fail( service.toString() );

        assertEquals( 1, (int) service.count() );

        final User pulled = service.findAll().get( 0 );

        assertEquals( c1.getRole(), pulled.getRole() );
        assertEquals( c1.getPassword(), pulled.getPassword() );
        assertEquals( c1.getUsername(), pulled.getUsername() );
        // assertEquals( c1, service.findAll().get( 0 ) );

        // Tests a duplicate username
        final Customer c2 = new Customer( "user", "pass" );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c2 ) ) ).andExpect( status().is4xxClientError() );
        assertEquals( 1, service.findAll().size(), "User with the username " + c2.getUsername() + " already exists." );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( null ) ) ).andExpect( status().is4xxClientError() );
    }

    @Test
    @Transactional
    public void testGetUsers () throws Exception {
        // TODO: update with the parameters
        mvc.perform( post( "/api/v1/users/" + c1.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/users/" + c1.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/users/" + "rando" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isNotFound() );

        // TODO: update with the parameters
        // final Customer c2 = new Customer();
        // mvc.perform( post( "/api/v1/users" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( c2 ) ) ).andExpect( status().isOk()
        // );

        // final String users = mvc.perform( get( "/api/v1/recipes" ) ).andDo(
        // print() ).andExpect( status().isOk() )
        // .andReturn().getResponse().getContentAsString();
        // TODO: update with the username of the customer
        // assertTrue( users.contains( "" ) );
        // TODO: update with the username of the customer
        // assertTrue( users.contains( "" ) );
    }

    @Test
    @Transactional
    public void testGetUser () throws Exception {
        mvc.perform( post( "/api/v1/users/" + c1.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() );

        final Customer c1 = new Customer();

    }

    @Test
    @Transactional
    public void testDeleteUser () throws Exception {
        // TODO: fix fields
        mvc.perform( post( "/api/v1/users/" + c1.getUsername() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() );
        final String users = mvc.perform( get( "/api/v1/users" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        assertTrue( users.contains( c1.getUsername() ) );

        final String deleteStatusMessage = mvc
                .perform( delete( String.format( "/api/v1/users/%s", c1.getUsername() ) ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void tempTes () throws Exception {

        mvc.perform( post( "/api/v1/generateUsers" ) ).andExpect( status().isCreated() );

        assertEquals( 5, service.count() );

    }
}
