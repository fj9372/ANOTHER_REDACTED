package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.ufund.api.ufundapi.persistence.BasketDAO;
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
@RequestMapping("baskets")
public class BasketController {
    private static final Logger LOG = Logger.getLogger(BasketController.class.getName());
    private BasketDAO basketDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param petDao The {@link petDAO Pet Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public BasketController(BasketDAO basketDao) {
        this.basketDao = basketDao;
    }

    /**
     * Gets a {@linkplain Pet pet} with the provided username that has been adopted by the user
     * 
     * @param username - The {@link User user} that we want the pets from that's currently in their adopted basket
     * 
     * @return ResponseEntity with created {@link Pet pet} object and HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/adopted/{username}")
    public ResponseEntity<Pet[]> getAdoptedPet(@PathVariable String username) {
        LOG.info("GET /username/" + username);
        try {
            Pet[] pet = basketDao.getAdoptedPet(username);
            return new ResponseEntity<Pet[]>(pet,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a {@linkplain Pet pet} with the provided username
     * 
     * @param username - The {@link User user} that we want the pets from that's currently in their basket
     * 
     * @return ResponseEntity with created {@link Pet pet} object and HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Pet[]> getPet(@PathVariable String username) {
        LOG.info("GET /username/" + username);
        try {
            Pet[] pet = basketDao.getPet(username);
            return new ResponseEntity<Pet[]>(pet,HttpStatus.OK);
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
            Pet newPet = basketDao.createPet(pet);
            if(newPet != null){
                return new ResponseEntity<Pet>(newPet,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Pet pet} with the provided pet object that will be added to the adopted basket
     * 
     * @param pet - The {@link Pet pet} to create for adopting
     * 
     * @return ResponseEntity with created {@link Pet pet} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Pet pet} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/adopt")
    public ResponseEntity<Pet> adoptPet(@RequestBody int id) {
        LOG.info("POST ADOPT /pets ");
        try{
            boolean newPet = basketDao.adoptPet(id);
            if(newPet == true){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
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
        LOG.info("DELETE BASKET /pets/" + id);
        try{
            boolean petDeleted = basketDao.deletePet(id);
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
            Pet pet = basketDao.getPet(id);
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
            Pet[] pets = basketDao.getPets();
            return new ResponseEntity<Pet[]>(pets,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
