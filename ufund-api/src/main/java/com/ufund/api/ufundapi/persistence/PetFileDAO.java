package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Pet;

/**
 * Implements the functionality for JSON file-based peristance for Pet
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Group E
 */
@Component
public class PetFileDAO implements PetDAO {
    Map<Integer,Pet> pets;   // Provides a local cache of the Pet objects
                                // so that we don't need to read from the file
                                // each time
    Map<Integer,Pet> petList;
    private ObjectMapper objectMapper;  // Provides conversion between Pet
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new Pet
    private String filename;    // Filename to read from and write to
    private String filename2;

    /**
     * Creates a Pet File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public PetFileDAO(@Value("${pets.file}") String filename, @Value("${petList.file}") String filename2, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.filename2 = filename2;
        this.objectMapper = objectMapper;
        load();  // load the Pet from the file
    }

    /**
     * Generates the next id for a new {@linkplain Pet Pet}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Pet Pet} from the tree map
     * 
     * @return  The array of {@link Pet Pet}, may be empty
     */
    private Pet[] getPetsArray() {
        return getPetsArray(null);
    }

    private Pet[] getPetListArray(){
        ArrayList<Pet> petArrayList = new ArrayList<>();

        for (Pet pet : petList.values()) {
            petArrayList.add(pet); 
        }

        Pet[] petArray = new Pet[petArrayList.size()];
        petArrayList.toArray(petArray);
        return petArray;
    }

    /**
     * Generates an array of {@linkplain Pet Pet} from the tree map for any
     * {@linkplain Pet Pet} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Pet Pet}
     * in the tree map
     * 
     * @return  The array of {@link Pet Pet}, may be empty
     */
    private Pet[] getPetsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Pet> petArrayList = new ArrayList<>();

        for (Pet pet : pets.values()) {
            if (containsText == null || pet.getName().toLowerCase().startsWith(containsText.toLowerCase())) {
                petArrayList.add(pet);
            }
        }

        Pet[] petArray = new Pet[petArrayList.size()];
        petArrayList.toArray(petArray);
        return petArray;
    }

    /**
     * Generates an array of {@linkplain Pet Pet} from the tree map for any
     * {@linkplain Pet Pet} that contains the type specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Pet Pet}
     * in the tree map
     * 
     * @return  The array of {@link Pet Pet}, may be empty
     */
    private Pet[] getPetsArrayByType(String containsText) {
        ArrayList<Pet> petArrayList = new ArrayList<>();

        for (Pet pet : pets.values()) {
            if (pet.getAnimaltype().toLowerCase().startsWith(containsText.toLowerCase())) {
                petArrayList.add(pet);
            }
        }

        Pet[] petArray = new Pet[petArrayList.size()];
        petArrayList.toArray(petArray);
        return petArray;
    }

    /**
     * Saves the {@linkplain Pet Pet} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Pet Pet} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Pet[] petArray = getPetsArray();
        Pet[] petListArray = getPetListArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),petArray);
        objectMapper.writeValue(new File(filename2), petListArray);
        return true;
    }

    /**
     * Loads {@linkplain Pet Pet} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        pets = new TreeMap<>();
        petList = new TreeMap<>();
        nextId = 0;
        // Deserializes the JSON objects from the file into an array of Pet
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Pet[] petsArray = objectMapper.readValue(new File(filename),Pet[].class);
        Pet[] petListArray = objectMapper.readValue(new File(filename2),Pet[].class);
        // Add each Pet to the tree map and keep track of the greatest id
        for (Pet pet : petsArray) {
            pets.put(pet.getId(),pet);
            if (pet.getId() > nextId)
                nextId = pet.getId();
        }

        for (Pet pet: petListArray) {
            petList.put(pet.getId(), pet);
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet[] getPets() {
        synchronized(pets) {
            return getPetsArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet[] findPets(String containsText) {
        synchronized(pets) {
            return getPetsArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet[] findPetsByType(String containsText) {
        synchronized(pets) {
            return getPetsArrayByType(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet getPet(int id) {
        synchronized(pets) {
            if (petList.containsKey(id))
                return petList.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet getCurrentPet(int id) {
        synchronized(pets) {
            if (pets.containsKey(id))
                return pets.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet createPet(Pet pet) throws IOException {
        synchronized(pets) {
            // We create a new Pet object because the id field is immutable
            // and we need to assign the next unique id
            Pet newPet = new Pet(nextId(),pet.getAnimaltype(), pet.getName());
            pets.put(newPet.getId(),newPet);
            petList.put(newPet.getId(),newPet);
            save(); // may throw an IOException
            return newPet;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet updatePet(Pet pet) throws IOException {
        synchronized(pets) {
            if (pets.containsKey(pet.getId()) == false)
                return null;  // Pet does not exist

            pets.put(pet.getId(),pet);
            petList.put(pet.getId(), pet);
            save(); // may throw an IOException
            return pet;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deletePet(int id) throws IOException {
        synchronized(pets) {
            if (pets.containsKey(id)) {
                pets.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    public void setPets(Map<Integer, Pet> pets){
        this.pets=pets;
    }
}
