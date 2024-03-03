package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author Group 5E
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[4];
        int[] basket= {};
        int[] adopt = {};
        String[] notifs = {};
        testUsers[1] = new User("Hyper", "HyperIsDum", basket, adopt, notifs);
        testUsers[2] = new User("Tenshi", "TenshiIsDum", basket, adopt, notifs);
        testUsers[0] = new User("Beep", "Boop", basket, adopt, notifs);
        testUsers[3] = new User("admin", "admin", basket, adopt, new String[]{"Test2","Test1", "Test1"});

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the user array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);

    }

    @Test
    public void testGetUsers() {
        // Invoke
        User[] users = userFileDAO.getUsers();

        // Analyze
        assertEquals(users.length,testUsers.length);
        for (int i = 0; i < testUsers.length;++i)
            assertEquals(users[i],testUsers[i]);
    }

    @Test
    public void testGetUser() throws IOException {
        // Invoke
        User user = userFileDAO.getUser("Beep");

        // Analzye
        assertEquals(user,testUsers[0]);
    }

    @Test
    public void testCheckUser() {
        // Invoke
        User user = userFileDAO.checkUser("Beep", "Boop");
        User user2 = userFileDAO.checkUser("Beep", "Derp");
        // Analzye
        assertEquals(user,testUsers[0]);
        assertEquals(user2, null);
    }


    @Test
    public void testCreateUser() throws IOException {
        // Setup
        int[] basket= {};
        int[] adopt = {};
        String[] notifs = {};
        User user = new User("Hoop", "Beep", basket, adopt, notifs);

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        assertEquals(result.getUsername(),user.getUsername());
        assertEquals(result.getPassword(),user.getPassword());
    }

    @Test
    public void testGetNotification() throws IOException{
        String[] notifs = this.userFileDAO.getNotifications("Hyper");
        assertEquals(notifs.length, 0);
    }

    @Test
    public void testGetNotificationNull() throws IOException{
        String[] notifs = this.userFileDAO.getNotifications("Hyperrr");
        assertEquals(notifs, null);
    }

    @Test
    public void testAddNotifications() throws IOException{
        String notif = this.userFileDAO.addNotification("Test");
        assertEquals(notif, "Test");
    }

    @Test
    public void testDeleteNotifications() throws IOException{
        this.userFileDAO.checkUser("admin", "admin");
        boolean deleted = this.userFileDAO.deleteNotification(("Test1"));
        assertEquals(deleted, true);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        int[] basket= {};
        int[] adopt = {};
        String[] notifs = {};
        User user = new User("Boop", "Beep", basket, adopt, notifs);

        assertThrows(IOException.class,
                        () -> userFileDAO.createUser(user),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        // Invoke
        User user = userFileDAO.getUser("Random");

        // Analyze
        assertEquals(user,null);
    }

    @Test
    public void testCheckUserNotFound() {
        // Invoke
        User user = userFileDAO.checkUser("Random", "Random");

        // Analyze
        assertEquals(user,null);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
