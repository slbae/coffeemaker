package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 *
 * The APIUserController is responsible for handling the User portions of the
 * application
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIUserController extends APIController {

    /**
     * The user service that keeps track of all user's
     */
    @Autowired
    private UserService service;

    // private UserManager manager;

    // getUsers
    /**
     * REST API method to provide GET access to all users in the system
     *
     * @return JSON representation of all users
     */
    @GetMapping ( BASE_PATH + "/users" )
    public List<User> getUsers () {
        return service.findAll();
    }

    /**
     * REST API method that returns a user based on the username
     *
     * @param username
     *            the User's username
     *
     * @return JSON representation of the user
     */
    @GetMapping ( BASE_PATH + "/users/{username}" )
    public ResponseEntity getUser ( @PathVariable ( "username" ) final String username ) {
        final User user = service.findByName( username );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with username " + username ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * Returns the current user's id number
     *
     * @param session
     *            the HTTPSession which contains a currentUserName attribute
     *            assigned by the security framework
     * @return the current user's id number
     */
    @GetMapping ( "/api/user/current" )
    public Long getCurrentUserID ( final HttpSession session ) {
        final String currentUserName = (String) session.getAttribute( "userName" );
        return (Long) service.findByName( currentUserName ).getId();
    }

    //
    // @PutMapping ( BASE_PATH + "/users/{username}" )
    // public ResponseEntity editUser ( @PathVariable final String username,
    // @RequestBody final User editedUser ) {

    // Pull the original recipe from the recipeService
    // final User originalUser = userService.findByName( username );

    // Check that the user actually exists
    // if ( null == originalUser ) {
    // return new ResponseEntity( errorResponse( "No recipe found for name " +
    // editedRecipe.getName() ),
    // HttpStatus.NOT_FOUND );
    // }
    // had in iTrust, check ids from original user and edited are the
    // same. dunno if necessary
    // if ( null != editedUser.getId() && !username.equals(
    // editedUser.getId() ) ) {
    // return new ResponseEntity( errorResponse( "The ID provided does not
    // match the ID of the User provided" ),
    // HttpStatus.CONFLICT );
    // }

    // Use the updateRecipe method to modify the Recipe
    // originalUser.updateUser( editedUser );

    // Save the newly updated Recipe
    // userService.save( originalUser );

    // Return a successful message
    // return new ResponseEntity( successResponse( editedUser.getName() + " was
    // edited successfully" ),
    // HttpStatus.OK );

    // }

    // deleteUser
    /**
     * REST API endpoint to delete a user from the system
     *
     * @param username
     *            the user's username
     * @return HTTP Response of NOT_FOUND(404) or OK (200)
     */
    @DeleteMapping ( BASE_PATH + "users/{username}" )
    public ResponseEntity deleteUser ( @PathVariable final String username ) {
        final User user = service.findByName( username );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + username ), HttpStatus.NOT_FOUND );
        }
        service.delete( user );

        return new ResponseEntity( successResponse( username + " was deleted successfully" ), HttpStatus.OK );
    }

    // addUser
    /**
     * REST API endpoint to create a user in the sytsem
     *
     * @param user
     *            the user to be added to the system
     * @return HTTP Response of OK (200) or CONFLICT (409)
     */
    @PostMapping ( BASE_PATH + "users/{username}" )
    public ResponseEntity createUser ( @RequestBody final User user ) {

        if ( service == null || user == null || user.getUsername() == null
                || service.findByName( user.getUsername() ) != null ) {
            return new ResponseEntity(
                    errorResponse( "User with the username " + user.getUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        // : if breaks, might need to work with the roles, depends on if
        // user object allowed
        service.save( user );
        return new ResponseEntity( successResponse( user.getUsername() + " successfully created" ), HttpStatus.OK );
    }

    // login
    // Change all to RequestParam and go from there?
    // @GetMapping ( BASE_PATH + "users/login/{username}" )
    // public ResponseEntity login ( @PathVariable ( "username" ) final String
    // username,
    // @RequestParam ( "password" ) final String password ) {
    // final User user = service.findByName( username );
    // if ( user == null ) {
    // return new ResponseEntity( errorResponse( "No user found for name " +
    // username ), HttpStatus.NOT_FOUND );
    // }
    // if ( user.getPassword().equals( password ) ) {
    // if ( user.isLoggedIn() ) {
    // return new ResponseEntity( errorResponse( "User already logged in already
    // exists" ),
    // HttpStatus.CONFLICT );
    // }
    //
    // user.logIn();
    // return new ResponseEntity<>( user, HttpStatus.OK );
    // // return new ResponseEntity( successResponse( user.getRole() ),
    // // HttpStatus.OK );
    // }
    // return new ResponseEntity( errorResponse( "Invalid Password Provided" ),
    // HttpStatus.UNAUTHORIZED );
    // }

    // // logout
    // @GetMapping ( BASE_PATH + "users/{username}/logout" )
    // public ResponseEntity logout ( @PathVariable final String username ) {
    // final User user = service.findByName( username );
    // if ( null == user ) {
    // return new ResponseEntity( errorResponse( "No user found for name " +
    // username ), HttpStatus.NOT_FOUND );
    // }
    // if ( !user.isLoggedIn() ) {
    // return new ResponseEntity( errorResponse( "User already logged out" ),
    // HttpStatus.CONFLICT );
    // }
    // user.logOut();
    //
    // return new ResponseEntity( successResponse( user.getUsername() + "
    // successfully logged out" ), HttpStatus.OK );
    // }

    /**
     * Generate a bunch of logins
     *
     * @return CREATED Response Entity
     */
    @PostMapping ( BASE_PATH + "generateUsers" )
    public ResponseEntity generateUsers () {

        service.deleteAll();

        final User u1 = new User( "Helen", "pass", "Staff" );
        final User u2 = new User( "Sylbi", "password", "Customer" );
        final User u3 = new User( "Mikyla", "word", "Customer" );
        final User u4 = new User( "Tyler", "sswo", "Staff" );
        final User u5 = new User( "Caleb", "drowssap", "Staff" );

        service.save( u1 );
        service.save( u2 );
        service.save( u3 );
        service.save( u4 );
        service.save( u5 );

        return new ResponseEntity( HttpStatus.CREATED );
    }
}
