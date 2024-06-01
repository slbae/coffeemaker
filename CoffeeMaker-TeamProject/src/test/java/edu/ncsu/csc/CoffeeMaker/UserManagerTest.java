// package edu.ncsu.csc.CoffeeMaker;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;
//
// import java.util.List;
//
// import javax.transaction.Transactional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
//
// import edu.ncsu.csc.CoffeeMaker.models.User;
// import edu.ncsu.csc.CoffeeMaker.services.UserService;
//
// @SpringBootTest
// @AutoConfigureMockMvc
// @ExtendWith ( SpringExtension.class )
// public class UserManagerTest {
//
// @Autowired
// private UserService userService;
//
// // @Autowired
// private UserManager manager;
//
// @BeforeEach
// public void beforeEach () {
// manager = UserManager.getInstance();
// }
//
// @Test
// @Transactional
// public void testcreateUser () {
// final List<User> users = userService.findAll();
// assertEquals( 5, users.size() );
// assertTrue( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// assertFalse( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// final User user = userService.findByName( "name" );
// userService.delete( user );
// }
//
// @Test
// @Transactional
// public void testcreateUser2 () {
// final List<User> users = userService.findAll();
// assertEquals( 5, users.size() );
// assertTrue( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// assertFalse( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// final User user = userService.findByName( "name" );
// userService.delete( user );
// }
//
// @Test
// @Transactional
// public void testGetUsers () {
// final List<User> users = manager.getUsers();
// assertEquals( 5, users.size() );
// assertTrue( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// assertFalse( manager.createUser( "name2", "pass", "Staff" ) );
// assertEquals( 7, users.size() );
// boolean foundUser1 = false;
// boolean foundUser2 = false;
// final User user1 = userService.findByName( "name" );
// final User user2 = userService.findByName( "name2" );
// for ( int i = 0; i < users.size(); i++ ) {
// if ( users.get( i ).equals( user1 ) ) {
// foundUser1 = true;
// }
// if ( users.get( i ).equals( user2 ) ) {
// foundUser2 = true;
// }
// }
// assertTrue( foundUser1 );
// assertTrue( foundUser2 );
// userService.delete( user1 );
// userService.delete( user2 );
// }
//
// @Test
// @Transactional
// public void testGetUser () {
// final List<User> users = manager.getUsers();
// assertEquals( 5, users.size() );
// assertTrue( manager.createUser( "name", "pass", "Staff" ) );
// assertEquals( 6, users.size() );
// assertFalse( manager.createUser( "name2", "pass", "Staff" ) );
// assertEquals( 7, users.size() );
// final User user1 = userService.findByName( "name" );
// final User user2 = userService.findByName( "name2" );
// assertEquals( user1, manager.findUser( "name" ) );
// assertEquals( user1, manager.findUser( "name2" ) );
// userService.delete( user1 );
// userService.delete( user2 );
// }
// }
