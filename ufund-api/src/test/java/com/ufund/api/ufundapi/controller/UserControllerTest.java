package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.Pet;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 * 
 * @author Group 5E
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetUser() throws IOException {  // getUser may throw IOException
        // Setup
        int[] basket = {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Hyper", "HyperIsDum", basket, adopt, messages);
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserWithPasswordException() throws IOException {  // getUser may throw IOException
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.checkUser(user.getUsername(), user.getPassword())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        String username = "Hyper";
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        when(mockUserDAO.getUser(username)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserNotFoundWithPassword() throws Exception { // createUser may throw IOException
        // Setup
        String username = "Hyper";
        String password = "HyperIsDum";
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        when(mockUserDAO.checkUser(username, password)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username, password);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { // createUser may throw IOException
        // Setup
        String userId = "Hyper";
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleExceptionWithPassword() throws Exception { // createUser may throw IOException
        // Setup
        String userId = "Hyper";
        String password = "HyperIsDum";
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).checkUser(userId, password);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId, password);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testCreateUserNull() throws IOException {  // createUser may throw IOException
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        // Invoke
        ResponseEntity<User> response = userController.createUser(user);
        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());

    }

    @Test
    public void testCreateNotifs() throws IOException{
        when(mockUserDAO.addNotification("Hello")).thenReturn("");
        ResponseEntity<String> response = userController.createNotification("Hello");
        assertEquals(HttpStatus.CREATED,response.getStatusCode()); 
    }

    @Test
    public void testCreateNotifsException() throws IOException{
        doThrow(new IOException()).when(mockUserDAO).addNotification("Hello");
        ResponseEntity<String> response = userController.createNotification("Hello");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNotifs() throws IOException{
        when(mockUserDAO.deleteNotification("Hello")).thenReturn(true);
        ResponseEntity<Pet> response = userController.deleteNotification("Hello");
        assertEquals(HttpStatus.OK,response.getStatusCode()); 
    }
    @Test
    public void testDeleteNotifsTwo() throws IOException{
        when(mockUserDAO.deleteNotification("Hello")).thenReturn(false);
        ResponseEntity<Pet> response = userController.deleteNotification("Hello");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode()); 
    }
    @Test
    public void testDeleteNotifsException() throws IOException{
        doThrow(new IOException()).when(mockUserDAO).deleteNotification("Hello");
        ResponseEntity<Pet> response = userController.deleteNotification("Hello");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode()); 
    }

    @Test
    public void testGetUserNotifs() throws IOException{
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        when(mockUserDAO.getNotifications(user.getUsername())).thenReturn(new String[]{});
        ResponseEntity<String[]> response = userController.getUserNotifications(user.getUsername());
        assertEquals(HttpStatus.OK,response.getStatusCode());  
    }

    @Test
    public void testGetUserNotifsNull() throws IOException{
        ResponseEntity<String[]> response = userController.getUserNotifications("TestName");
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNotifException() throws IOException{
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Hyper", "HyperIsDum", basket, adopt, messages);

        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getNotifications(user.getUsername());

        // Invoke
        ResponseEntity<String[]> response = userController.getUserNotifications(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateUserFailed() throws IOException {  // createUser may throw IOException
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  // createUser may throw IOException
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User("Hyper", "HyperIsDum", basket, adopt, messages);

        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException { // getUsers may throw IOException
        // Setup
        User[] users = new User[2];
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        users[0] = new User("Hyper", "HyperIsDum", basket, adopt, messages);
        users[1] = new User("Tenshi", "TenshiIsDum", basket, adopt, messages);
        // When getUsers is called return the users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testGetUsersHandleException() throws IOException { // getUsers may throw IOException
        // Setup
        // When getUsers is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}
