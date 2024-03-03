package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the User class
 * 
 * @author Group 5E
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup

        String expected_username = "Hyper";
        String expected_password = "HyperIsDum";
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        // Invoke
        User user = new User(expected_username, expected_password, basket, adopt, messages);

        // Analyze
        assertEquals(expected_password,user.getPassword());
        assertEquals(expected_username,user.getUsername());
    }

    @Test
    public void testSetUsername() {
        // Setup
        String username = "Hyper";
        String password = "HyperIsDum";
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User(username, password, basket, adopt, messages);

        String expected_name = "Tenshi";

        // Invoke
        user.setUsername(expected_name);

        // Analyze
        assertEquals(expected_name,user.getUsername());
    }
    @Test
    public void testSetPassword(){
        //Setup
        String username = "Tenshi";
        String password = "TenshiIsDum";
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User(username, password, basket, adopt, messages);

        String expected_password = "Test";
        //Invoke
        user.setPassword(expected_password);
        //Analyze
        assertEquals(expected_password,user.getPassword());
    }

    @Test
    public void testToString() {
        // Setup
        String username = "Hyper";
        String password = "HyperIsDum";
        int[] basket= {};
        int[] adopt = {};
        String expected_string = String.format(User.STRING_FORMAT,username,password);
        String[] messages = {};
        User user = new User(username, password, basket, adopt, messages);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    public void testSetBasket(){
        //Setup
        String username = "Tenshi";
        String password = "TenshiIsDum";
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User(username, password, basket, adopt, messages);

        int[] expected_basket = {1,2,3};
        //Invoke
        user.setBasket(expected_basket);
        //Analyze
        assertEquals(expected_basket, user.getBasket_pets());
    }

    @Test
    public void testSetAdopted(){
        //Setup
        String username = "Tenshi";
        String password = "TenshiIsDum";
        int[] basket= {};
        int[] adopt = {};
        String[] messages = {};
        User user = new User(username, password, basket, adopt, messages);

        int[] expected_adopted = {1,2,3};
        //Invoke
        user.setAdopted(expected_adopted);
        //Analyze
        assertEquals(expected_adopted, user.getAdopted_pets());
    }

    @Test
    public void testGetNotifs(){
        String username = "Derp";
        String password = "Derpy";
        int[] basket= {};
        int[] adopt = {};
        User user = new User(username, password, basket, adopt, null);
        String[] notifs = user.getNotifications();
        assertEquals(0, notifs.length);
    }

}