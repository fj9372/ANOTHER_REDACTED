package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Pet;

/**
 * Defines the interface for Pet object persistence
 * 
 * @author Group E
 */
public interface PetDAO {
    /**
     * Retrieves all {@linkplain Pet Pets}
     * 
     * @return An array of {@link Pet Pet} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] getPets() throws IOException;

    /**
     * Finds all {@linkplain Pet Pets} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Pet Pets} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] findPets(String containsText) throws IOException;

    /**
     * Finds all {@linkplain Pet Pets} whose type contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Pet Pets} whose types contains the given text
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet[] findPetsByType(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Pet Pet} with the given id from the complete list of pets
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

    /**
     * Retrieves a {@linkplain Pet Pet} with the given id from the current available list
     * 
     * @param id The id of the {@link Pet Pet} to get
     * 
     * @return a {@link Pet Pet} object with the matching id
     * <br>
     * null if no {@link Pet Pet} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Pet getCurrentPet(int id) throws IOException;

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
     * Updates and saves a {@linkplain Pet Pet}
     * 
     * @param {@link Pet Pet} object to be updated and saved
     * 
     * @return updated {@link Pet Pet} if successful, null if
     * {@link Pet Pet} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Pet updatePet(Pet Pet) throws IOException;

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


}
