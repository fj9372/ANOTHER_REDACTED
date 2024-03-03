package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.User;

/**
 * Defines the interface for User object persistence
 * 
 * @author Group E
 */
public interface UserDAO {
    /**
     * Retrieves all {@linkplain User Users}
     * 
     * @return An array of {@link User User} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    User[] getUsers() throws IOException;



    /**
     * Retrieves a {@linkplain User User} with the given id
     * 
     * @param id The id of the {@link User User} to get
     * 
     * @return a {@link User User} object with the matching id
     * <br>
     * null if no {@link User User} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getUser(String username) throws IOException;

    /**
     * Retrieves notifications with from the given username
     * 
     * @param username The username of the {@link User User} to get the notifications from
     * 
     * @return an array of String object for the matching username
     * <br>
     * null if no {@link User User} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    String[] getNotifications(String username) throws IOException;

    /**
     * Retrieves a {@linkplain User User} with the given username and password
     * 
     * @param username The username of the {@link User User} to get
     * @param password The password of the {@link User User} to get
     * 
     * @return a {@link User User} object with the matching username and password
     * <br>
     * null if no {@link User User} with a matching username and password is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User checkUser(String username, String password) throws IOException;

    /**
     * Creates and saves a {@linkplain User User}
     * 
     * @param User {@linkplain User User} object to be created and saved
     * <br>
     * The id of the User object is ignored and a new uniqe id is assigned
     *
     * @return new {@link User User} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    User createUser(User user) throws IOException;

    /**
     * Add a notification to admin
     * 
     * @param message The notification to be added
     * 
     * @return the message if added successfully
     * <br>
     * 
     * @throws IOException if an issue with underlying storage
     */
    String addNotification(String message) throws IOException;

    /**
     * Deletes a notification from the current user
     * 
     * @param message The notification to be deleted
     * 
     * @return true if added successfully, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean deleteNotification(String message) throws IOException;
}
