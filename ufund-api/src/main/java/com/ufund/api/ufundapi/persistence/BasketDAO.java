package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Pet;

/**
 * Defines the interface for Pet object persistence
 * 
 * @author Group E
 */
public interface BasketDAO {
    /**
     * Retrieves all {@linkplain Pet Pets}
     * 
     * @return An array of {@link Pet Pet} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] getPets() throws IOException;

    /**
     * Retrieves all {@linkplain Pet Pets} from the adopted basket
     * 
     * @return An array of {@link Pet Pet} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] getAdoptedPets() throws IOException;


     /**
     * Retrieves a {@linkplain Pet Pet} with the given username
     * 
     * @param username The username of the {@link Pet Pet} to get
     * 
     * @return a {@link Pet Pet} object with the matching username
     * <br>
     * null if no {@link Pet Pet} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] getPet(String user) throws IOException;

    /**
     * Retrieves a {@linkplain Pet Pet} with the given username from the adopted list
     * 
     * @param username The username of the {@link Pet Pet} to get
     * 
     * @return a {@link Pet Pet} object with the matching username
     * <br>
     * null if no {@link Pet Pet} with a matching username is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] getAdoptedPet(String user) throws IOException;

    /**
     * Creates and saves a {@linkplain Pet Pet}
     * 
     * @param Pet {@linkplain Pet Pet} object to be created and saved
     * <br>
     * The id of the Pet object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Pet Pet} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet createPet(Pet Pet) throws IOException;

    /**
     * Deletes a {@linkplain Pet Pet} with the given id and add it to adoption basket
     * 
     * @param id The id of the {@link Pet Pet}
     * 
     * @return true if the {@link Pet Pet} was deleted
     * <br>
     * false if Pet with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean adoptPet(int id) throws IOException;

    /**
     * Deletes a {@linkplain Pet Pet} with the given id
     * 
     * @param id The id of the {@link Pet Pet}
     * 
     * @return true if the {@link Pet Pet} was deleted
     * <br>
     * false if Pet with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deletePet(int id) throws IOException;

    /**
     * Retrieves a {@linkplain Pet Pet} with the given id
     * 
     * @param id The id of the {@link Pet Pet} to get
     * 
     * @return a {@link Pet Pet} object with the matching id
     * <br>
     * null if no {@link Pet Pet} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet getPet(int id) throws IOException;


}
