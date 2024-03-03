package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a User(need) entity
 *
 */
public class User {


    // Package private for tests
    static final String STRING_FORMAT = "User [username = %s, password = %s]";

    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("basket_pets") private int[] basket_pets;
    @JsonProperty("adopted_pets") private int[] adopted_pets;
    @JsonProperty("notifications") private String[] notifications;

    /**
     * Create a User with the given username, password
     * @param id The id of the user
     * @param animaltype The type of animal the user is
     * @param name The name of the user
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("basket_pets") int[] basket_pets, @JsonProperty("adopted_pets") int[] adopted_pets, @JsonProperty("notifications") String[] notifications) {
        this.username = username;
        this.password = password;
        this.basket_pets = basket_pets;
        this.adopted_pets = adopted_pets;
        this.notifications = notifications;
    }

    /**
     * Sets the name of the user 
     * @param username The username of the user
     */
    public void setUsername(String username) {this.username = username;}
    /**
     * Sets the password of the user
     * @param password The password of the user
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * Sets the basket_pets of the user 
     * @param basket The basket_pets of the user
     */
    public void setBasket(int[] basket) {this.basket_pets = basket;}

    /**
     * Sets the adopted_pets of the user 
     * @param adopted The aadopted_pets of the user
     */
    public void setAdopted(int[] adopted) {this.adopted_pets = adopted;}

    /**
     * Sets the notifications of the user 
     * @param notifications The notifications of the user
     */
    public void setNotifications(String[] notifications) {this.notifications = notifications;}

    /**
     * Retrieves the name of the user
     * @return The name of the user
     */
    public String getUsername() {return username;}

    /**
     * Retrives the password of the user
     * @return the password of the user
     */
    public String getPassword() {return password;}

    /**
     * Retrives the pets in the user's basket 
     * @return the pets in the user's basket
     */
    public int[] getBasket_pets() {return basket_pets;}

    /**
     * Retrives the pets the user has adopted
     * @return the pets the user has adopted
     */
    public int[] getAdopted_pets() {return adopted_pets;}

    /**
     * Retrives the notifications the user has 
     * @return the notifications the user has
     */
    public String[] getNotifications() {
        if(notifications == null){
            return new String[]{};
        }
        else{
            return notifications;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, password);
    }
}