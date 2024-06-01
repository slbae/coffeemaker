// package edu.ncsu.csc.CoffeeMaker;
//
// import java.util.List;
//
// import javax.persistence.Entity;
//
// import org.springframework.beans.factory.annotation.Autowired;
//
// import edu.ncsu.csc.CoffeeMaker.models.Customer;
// import edu.ncsu.csc.CoffeeMaker.models.Staff;
// import edu.ncsu.csc.CoffeeMaker.models.User;
// import edu.ncsu.csc.CoffeeMaker.services.UserService;
//
/// **
// *
// */
// @Entity
// public class UserManager {
// /**
// *
// */
// private User currentUser;
// /**
// *
// */
// @Autowired
// private UserService service;
//
// private static UserManager instance;
//
// /**
// *
// * @return
// */
// public List<User> getUsers () {
// return service.findAll();
// }
//
// public UserManager () {
// createUser( "Helen", "pass", "Staff" );
// createUser( "Sylbi", "password", "Customer" );
// createUser( "Mikyla", "word", "Customer" );
// createUser( "Tyler", "sswo", "Staff" );
// createUser( "Caleb", "drowssap", "Staff" );
// }
//
// public static UserManager getInstance () {
// if ( instance == null ) {
// instance = new UserManager();
// }
// return instance;
// }
//
// public User findUser ( final String username ) {
// return service.findByName( username );
// }
//
// public boolean login ( final String username ) {
// if ( currentUser != null ) {
// return false;
// }
// final User user = this.findUser( username );
// currentUser = user;
// return true;
// }
//
// public User getCurrentUser () {
// return currentUser;
// }
//
// public boolean logout () {
// if ( currentUser != null ) {
// return false;
// }
// currentUser = null;
// return true;
// }
//
// public boolean createUser ( final String username, final String password,
// final String role ) {
// if ( findUser( username ) != null ) {
// return false;
// }
// User user = null;
// if ( role.equals( "Staff" ) ) {
// user = new Staff( username, password );
// }
// else if ( role.equals( "Customer" ) ) {
// user = new Customer( username, password );
// }
// else {
// return false;
// }
// service.save( user );
// return true;
// }
// }
