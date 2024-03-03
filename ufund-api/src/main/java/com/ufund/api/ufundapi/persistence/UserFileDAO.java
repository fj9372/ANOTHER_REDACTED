package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for User
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Group E
 */
@Component
public class UserFileDAO implements UserDAO {
    Map<String,User> users;   // Provides a local cache of the User objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to
    private User current_user;

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the User from the file
    }
   

    /**
     * Generates an array of {@linkplain User User} from the tree map for any
     * {@linkplain User User} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User User}
     * in the tree map
     * 
     * @return  The array of {@link User User}, may be empty
     */
    public User[] getUsersArray() { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            userArrayList.add(user);
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }


    /**
     * Saves the {@linkplain User User} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User User} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads {@linkplain User User} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of User
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] usersArray = objectMapper.readValue(new File(filename),User[].class);
        // Add each User to the tree map and keep track of the greatest id
        for (User user : usersArray) {
            users.put(user.getUsername(),user);
        }
        // Make the next id one greater than the maximum from the file
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     * @throws IOException
     */
    @Override
    public User getUser(String username) throws IOException {
        load();
        synchronized(users) {
            if (users.containsKey(username))
                return users.get(username);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     * @throws IOException
     */
    @Override
    public String[] getNotifications(String username) throws IOException {
        load();
        synchronized(users) {
            if (users.containsKey(username))
                return users.get(username).getNotifications();
            else
                return null;
        }
    }
    /**
    ** {@inheritDoc}
     */
    @Override
    public User checkUser(String username, String password) {
        current_user = users.get(username);
        synchronized(users) {
            if ((users.containsKey(username)) && (users.get(username).getPassword().equals(password))){
                return users.get(username);
            }
            else{
                return null;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User createUser(User user) throws IOException {
        synchronized(users) {
            users.put(user.getUsername(),user);
            save(); // may throw an IOException
            return user;
        }
    }

     /**
    ** {@inheritDoc}
     */
    @Override
    public String addNotification(String message) throws IOException{
        load();
        synchronized(users) {
            User admin = this.getUser("admin");
            String[] currentNotifs = admin.getNotifications();
            String[] newNotifs = new String[currentNotifs.length+1];
            for(int i = 0; i < currentNotifs.length; i++){
                newNotifs[i] = currentNotifs[i];
            }
            newNotifs[currentNotifs.length] = message;
            admin.setNotifications(newNotifs);
            save();
            return message;
        }
    }
    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteNotification(String message) throws IOException{
        load();
        synchronized(users) {
            String[] currentNotifs = users.get(current_user.getUsername()).getNotifications();
            String[] newNotifs = new String[currentNotifs.length-1];
            boolean deleted = false;
            for(int i = 0, j = 0; i < currentNotifs.length; i++){
                if(deleted == false && currentNotifs[i].equals(message)){
                    deleted = true;
                }
                else{
                    newNotifs[j] = currentNotifs[i];
                    j++;
                }
            }
            users.get(current_user.getUsername()).setNotifications(newNotifs);
            return save(); // may throw an IOException
        }
    }

}
