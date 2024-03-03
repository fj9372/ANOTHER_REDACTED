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
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Pet
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Group E
 */
@Component
public class BasketFileDAO implements BasketDAO {
    Map<Integer,Pet> pets;   // Provides a local cache of the Pet objects
                                // so that we don't need to read from the file
                                // each time
    Map<Integer,Pet> adopted;
    private ObjectMapper objectMapper;  // Provides conversion between Pet
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to
    private String filename2;
    private String filename3;
    private UserFileDAO userFiledao;
    private PetFileDAO petFiledao;
    private PetFileDAO petList;
    private User current_user; // The currect user logged in

    /**
     * Creates a Pet File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public BasketFileDAO(@Value("${users.file}") String filename, @Value("${pets.file}") String filename2, @Value("${petList.file}") String filename3, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.filename2 = filename2;
        this.filename3 = filename3;
        this.userFiledao = new UserFileDAO(filename, objectMapper);
        this.petFiledao = new PetFileDAO(filename2, filename3, objectMapper);
        this.petList = new PetFileDAO(filename3, filename3, objectMapper);
        this.objectMapper = objectMapper;
        load();
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
    private Pet[] getPetsArray() { // if containsText == null, no filter
        ArrayList<Pet> petArrayList = new ArrayList<>();

        for (Pet pet : pets.values()) {
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
    private Pet[] getAdoptedPetsArray() { // if containsText == null, no filter
        ArrayList<Pet> petArrayList = new ArrayList<>();

        for (Pet pet : adopted.values()) {
            petArrayList.add(pet);
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

        User[] userArray = this.userFiledao.getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
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
        adopted = new TreeMap<>();
        if(current_user==null){
            return false;
        }
        // Deserializes the JSON objects from the file into an array of Pet
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);
        int[] petsArray = {};
        int[] adoptedArray = {};
        /*Get the basket and adopted basket of the current user */
        for (User user: userArray) {
            if (user.getUsername().equals(current_user.getUsername())){
                petsArray = current_user.getBasket_pets();
                adoptedArray = current_user.getAdopted_pets();
                break;
            }
        }
        /*Turn each integer of the basket into their respective pet*/
        for (int current_int : petsArray) {
            /*Check if the pet has been adopted already */
            if(this.petFiledao.getCurrentPet(current_int) != null){
                pets.put(current_int, petFiledao.getPet(current_int));
            }
            else{
                /*Modify the basket if a pet in there has been adopted */
                int [] old_list = current_user.getBasket_pets();
                int [] new_list = new int[old_list.length-1];
                for(int i = 0, j = 0; i < old_list.length; i++){
                    if(old_list[i] != current_int){
                        new_list[j] = old_list[i];
                        j++;
                    }
                }
                current_user.setBasket(new_list);
                /*Add a notification to the user about the adopted pet */
                String[] currentNotifs = current_user.getNotifications();
                String[] newNotifs = new String[currentNotifs.length+1];
                for(int i = 0; i < currentNotifs.length; i++){
                    newNotifs[i] = currentNotifs[i];
                 }
                newNotifs[currentNotifs.length] = petFiledao.getPet(current_int).getName() + " has been adopted";
                current_user.setNotifications(newNotifs);
                save();
            }
        }

        for (int current_int: adoptedArray) {
            adopted.put(current_int, petList.getPet(current_int));
        }
        // Make the next id one greater than the maximum from the file
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
    public Pet[] getAdoptedPets() {
        synchronized(pets) {
            return getAdoptedPetsArray();
        }
    }
    
    /**
    ** {@inheritDoc}
     * @throws IOException
     */
    @Override
    public Pet[] getPet(String user) throws IOException {
        current_user = userFiledao.getUser(user);
        this.petFiledao = new PetFileDAO(filename2, filename3, objectMapper);
        load();
        synchronized(pets) {
            return this.getPets();
        }
    }

    /**
    ** {@inheritDoc}
     * @throws IOException
     */
    @Override
    public Pet[] getAdoptedPet(String user) throws IOException {
        current_user = userFiledao.getUser(user);
        load();
        synchronized(pets) {
            return this.getAdoptedPets();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Pet createPet(Pet pet) throws IOException {
        synchronized(pets) {
            Pet newPet = new Pet(pet.getId(),pet.getAnimaltype(), pet.getName());
            /*Create a new basket so we can add another item in as int[] isn't mutable*/
            int[] current_basket = current_user.getBasket_pets();
            int[] temp_basket = new int[current_basket.length+1];
            for(int i = 0; i < current_basket.length; i++){
                if(current_basket[i] == pet.getId()){
                    return null;
                }
                temp_basket[i] = current_basket[i];
            }
            temp_basket[current_basket.length] = pet.getId();
            current_user.setBasket(temp_basket);
            pets.put(newPet.getId(),newPet);
            save(); // may throw an IOException
            return newPet;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deletePet(int id) throws IOException {
        synchronized(pets) {
            if (pets.containsKey(id)) {
                /*Create a new basket so we can remove an item from the old one as int[] isn't mutable*/
                int[] current_basket = current_user.getBasket_pets();
                int[] new_basket = new int[current_basket.length-1];
                for(int i=0,j=0;i<current_basket.length;i++){
                    if(current_basket[i] != id){
                        new_basket[j] = current_basket[i];
                        j++;
                    }
                }
                current_user.setBasket(new_basket);
                pets.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean adoptPet(int id) throws IOException {
        synchronized(pets) {
            synchronized(adopted){
                if (pets.containsKey(id)) {
                    /*Remove the pet from the basket*/
                    int[] current_basket = current_user.getBasket_pets();
                    int[] new_basket = new int[current_basket.length-1];
                    Pet newPet = pets.get(id);
                    for(int i=0,j=0;i<current_basket.length;i++){
                        if(current_basket[i] != id){
                            new_basket[j] = current_basket[i];
                            j++;
                        }
                    }
                    current_user.setBasket(new_basket);
                    pets.remove(id);
                    /*Add the pet to the adopted basket */
                    int[] current_Adopted = current_user.getAdopted_pets();
                    int[] temp_list = new int[current_Adopted.length+1];
                    for(int i = 0; i < current_Adopted.length; i++){
                        temp_list[i] = current_Adopted[i];
                    }
                    temp_list[current_Adopted.length] = newPet.getId();
                    current_user.setAdopted(temp_list);
                    adopted.put(newPet.getId(), newPet);
                    save();
                    /*Send notification to the admin */
                    String name = current_user.getUsername();
                    current_user = this.userFiledao.getUser("admin");
                    String[] currentNotifs = current_user.getNotifications();
                    String[] newNotifs = new String[currentNotifs.length+1];
                    for(int i = 0; i < currentNotifs.length; i++){
                        newNotifs[i] = currentNotifs[i];
                    }
                    newNotifs[currentNotifs.length] = name + " has adopted " + newPet.getName();
                    current_user.setNotifications(newNotifs);
                    save();
                    current_user = this.userFiledao.getUser(name);
                    return true;
                }
                else
                    return false;
            }
        }
    }

     /**
    ** {@inheritDoc}
     */
    @Override
    public Pet getPet(int id) {
        synchronized(pets) {
            if (pets.containsKey(id))
                return pets.get(id);
            else
                return null;
        }
    }


}
