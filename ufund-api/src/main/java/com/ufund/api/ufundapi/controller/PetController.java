package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level; 
import java.util.logging.Logger;
 

import com.ufund.api.ufundapi.persistence.PetDAO;
import com.ufund.api.ufundapi.model.Pet;

/**
 * Handles the REST API requests for the Pet resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Group E
 */

@RestController
@RequestMapping("pets")
public class PetController {
    private static final Logger LOG = Logger.getLogger(PetController.class.getName());
    private PetDAO petDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param petDao The {@link petDAO Pet Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public PetController(PetDAO petDao) {
        this.petDao = petDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Pet pet} for the given id
     * 
     * @param id The id used to locate the {@link Pet pet}
     * 
     * @return ResponseEntity with {@link Pet pet} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable int id) {
        LOG.info("GET /pets/" + id);
        try {
            Pet pet = petDao.getPet(id);
            if (pet != null)
                return new ResponseEntity<Pet>(pet,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Pet pets}
     * 
     * @return ResponseEntity with array of {@link Pet pet} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Pet[]> getPets() {
        LOG.info("GET /pets");
        try{
            Pet[] pets = petDao.getPets();
            return new ResponseEntity<Pet[]>(pets,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Pet pets} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Pet pets}
     * 
     * @return ResponseEntity with array of {@link Pet pet} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all pets that contain the text "ma"
     * GET http://localhost:8080/pets/name/ma
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Pet[]> searchPetsByName(@PathVariable String name) {
        LOG.info("GET /pets/name/"+name);
        try{
            Pet[] pets = petDao.findPets(name);
            return new ResponseEntity<Pet[]>(pets,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Pet pets} whose type contains
     * the type in name
     * 
     * @param type The type parameter which contains the text used to find the {@link Pet pets}
     * 
     * @return ResponseEntity with array of {@link Pet pet} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all pet types that contain the text "ma"
     * GET http://localhost:8080/pets/type/ma
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Pet[]> searchPetsByType(@PathVariable String type) {
        LOG.info("GET /pets/type/"+type);
        try{
            Pet[] pets = petDao.findPetsByType(type);
            return new ResponseEntity<Pet[]>(pets,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    /**
     * Creates a {@linkplain Pet pet} with the provided pet object
     * 
     * @param pet - The {@link Pet pet} to create
     * 
     * @return ResponseEntity with created {@link Pet pet} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Pet pet} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        LOG.info("POST /pets " + pet);
        try{
            Pet newPet = petDao.createPet(pet);
            return new ResponseEntity<Pet>(newPet,HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Pet pet} with the provided {@linkplain Pet pet} object, if it exists
     * 
     * @param pet The {@link Pet pet} to update
     * 
     * @return ResponseEntity with updated {@link Pet pet} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        LOG.info("PUT /pets " + pet);
        try{
            Pet newPet = petDao.updatePet(pet);
            if(newPet != null) {
                return new ResponseEntity<Pet>(newPet,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Pet pet} with the given id
     * 
     * @param id The id of the {@link Pet pet} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable int id) {
        LOG.info("DELETE /pets/" + id);
        try{
            boolean petDeleted = petDao.deletePet(id);
            if(petDeleted == true) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
