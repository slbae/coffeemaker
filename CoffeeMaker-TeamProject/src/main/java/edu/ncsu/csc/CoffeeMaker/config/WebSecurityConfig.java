package edu.ncsu.csc.CoffeeMaker.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Used iTrust2's security model found at this repository:
 * https://github.ncsu.edu/engr-csc326-staff/iTrust2-v11 and this guide for
 * using Security with Spring
 * https://github.com/spring-guides/gs-securing-web/tree/23731da2036af0905ba02cb09e1b6fd3d2a8c9b1
 *
 * Does not use any valid Password Encoding so we do use a deprecated method to
 * bypass it
 *
 * Also modified the iTrust version so that whenever a user authenticates with
 * the system, they are directed to their main landing page immediately
 *
 * @author Helen O'Connell
 * @author Caleb Twigg
 */
@SuppressWarnings ( "deprecation" )
@Configuration
@EnableWebSecurity ( debug = false )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * The datasource which is used for a connection
     */
    @Autowired
    DataSource dataSource;

    /**
     * Searches for users using a custom SQL query that is based on the format
     * of our SQL server and object services
     *
     * @param auth
     *            an AuthenticationManagerBuilder object that is used to execute
     *            the query commands
     * @throws Exception
     *             if there is an error with accessing the SQL server
     */
    @Autowired
    public void configureGlobal ( final AuthenticationManagerBuilder auth ) throws Exception {
        // Searches for users by their username, and checks them against their
        // password from a global perspective
        auth.jdbcAuthentication().dataSource( dataSource ).passwordEncoder( passwordEncoder() )
                .usersByUsernameQuery( "select username, password, 1 as enabled from user WHERE username = ?" )
                .authoritiesByUsernameQuery( "select username, role from user where username=?" );
    }

    /**
     * Determines how a user is able to access the system, and what happens once
     * they are authenticated. If a user is authenticated, they are sned to the
     * LoginSuccessHandler which will redirect the page according to the user's
     * role
     *
     * @param http
     *            the HttpSecurity object which represents the settings that we
     *            are updating
     * @throws Exception
     *             if there is an issue with updating the security settings, or
     *             with a user logging in
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        // Authenticate a user and use the CustomerLoginSuccessHandler() method
        // to send them to the proper main page
        final String allowed[] = { "/login", "/api/v1/generateUsers" };
        http.authorizeRequests().antMatchers( allowed ).permitAll().anyRequest().authenticated().and().formLogin()
                .loginPage( "/login" ).successHandler( new CustomLoginSuccessHandler() ) // This
                                                                                         // sends
                                                                                         // successful
                                                                                         // authentications
                                                                                         // to
                                                                                         // our
                                                                                         // customer
                                                                                         // handler
                .permitAll().and().logout().logoutSuccessUrl( "/?logout" ).permitAll().and().csrf().disable();

    }

    // Old Password encoder
    // we got issues that stored passwords weren't encoded as expected so we
    // commented it out
    // /**
    // * Bean used to generate a PasswordEncoder to hash the user-provided
    // * password.
    // *
    // * @return The password encoder.
    // */
    // @Bean
    // public PasswordEncoder passwordEncoder () {
    // return new BCryptPasswordEncoder();
    // }

    /**
     * This is a depreciated method, but it does not have any encoding for the
     * passwords so there are minimal issues
     *
     * @return the instance of the depreciated NoOpPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Establishes a DefaultAuthenticationEventPublisher object (we go off the
     * default)
     *
     * @return a new DefaultAuthenticationEventPublisher object
     */
    @Bean
    public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher () {
        return new DefaultAuthenticationEventPublisher();
    }
}
