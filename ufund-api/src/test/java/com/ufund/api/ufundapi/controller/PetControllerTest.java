package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.PetDAO;
import com.ufund.api.ufundapi.model.Pet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Pet Controller class
 * 
 * @author Group 5E
 */
@Tag("Controller-tier")
public class PetControllerTest {
    private PetController petController;
    private PetDAO mockPetDAO;

    /**
     * Before each test, create a new PetController object and inject
     * a mock Pet DAO
     */
    @BeforeEach
    public void setupPetController() {
        mockPetDAO = mock(PetDAO.class);
        petController = new PetController(mockPetDAO);
    }

    @Test
    public void testGetPet() throws IOException {  // getPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        // When the same id is passed in, our mock Pet DAO will return the Pet object
        when(mockPetDAO.getPet(pet.getId())).thenReturn(pet);

        // Invoke
        ResponseEntity<Pet> response = petController.getPet(pet.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pet,response.getBody());
    }

    @Test
    public void testGetPetNotFound() throws Exception { // createPet may throw IOException
        // Setup
        int petId = 99;
        // When the same id is passed in, our mock Pet DAO will return null, simulating
        // no pet found
        when(mockPetDAO.getPet(petId)).thenReturn(null);

        // Invoke
        ResponseEntity<Pet> response = petController.getPet(petId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetPetHandleException() throws Exception { // createPet may throw IOException
        // Setup
        int petId = 99;
        // When getPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).getPet(petId);

        // Invoke
        ResponseEntity<Pet> response = petController.getPet(petId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all PetController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreatePet() throws IOException {  // createPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Wi-Fire");
        // when createPet is called, return true simulating successful
        // creation and save
        when(mockPetDAO.createPet(pet)).thenReturn(pet);

        // Invoke
        ResponseEntity<Pet> response = petController.createPet(pet);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(pet,response.getBody());
    }

    @Test
    public void testCreatePetFailed() throws IOException {  // createPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Bolt");
        // when createPet is called, return false simulating failed
        // creation and save
        when(mockPetDAO.createPet(pet)).thenReturn(null);

        // Invoke
        ResponseEntity<Pet> response = petController.createPet(pet);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    public void testCreatePetHandleException() throws IOException {  // createPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Ice Gladiator");

        // When createPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).createPet(pet);

        // Invoke
        ResponseEntity<Pet> response = petController.createPet(pet);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdatePet() throws IOException { // updatePet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Wi-Fire");
        // when updatePet is called, return true simulating successful
        // update and save
        when(mockPetDAO.updatePet(pet)).thenReturn(pet);
        ResponseEntity<Pet> response = petController.updatePet(pet);
        pet.setName("Bolt");

        // Invoke
        response = petController.updatePet(pet);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pet,response.getBody());
    }

    @Test
    public void testUpdatePetFailed() throws IOException { // updatePet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        // when updatePet is called, return true simulating successful
        // update and save
        when(mockPetDAO.updatePet(pet)).thenReturn(null);

        // Invoke
        ResponseEntity<Pet> response = petController.updatePet(pet);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdatePetHandleException() throws IOException { // updatePet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        // When updatePet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).updatePet(pet);

        // Invoke
        ResponseEntity<Pet> response = petController.updatePet(pet);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetPets() throws IOException { // getPets may throw IOException
        // Setup
        Pet[] pets = new Pet[2];
        pets[0] = new Pet(99,"Greater sage-grouse", "Bolt");
        pets[1] = new Pet(99,"Greater sage-grouse", "The Great Iguana");
        // When getPets is called return the pets created above
        when(mockPetDAO.getPets()).thenReturn(pets);

        // Invoke
        ResponseEntity<Pet[]> response = petController.getPets();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pets,response.getBody());
    }

    @Test
    public void testGetPetsHandleException() throws IOException { // getPets may throw IOException
        // Setup
        // When getPets is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).getPets();

        // Invoke
        ResponseEntity<Pet[]> response = petController.getPets();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchPets() throws IOException { // findPets may throw IOException
        // Setup
        String searchString = "la";
        Pet[] pets = new Pet[2];
        pets[0] = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        pets[1] = new Pet(99,"Greater sage-grouse", "Ice Gladiator");
        // When findPets is called with the search string, return the two
        /// pets above
        when(mockPetDAO.findPets(searchString)).thenReturn(pets);

        // Invoke
        ResponseEntity<Pet[]> response = petController.searchPetsByName(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pets,response.getBody());
    }

    @Test
    public void testSearchPetsByType() throws IOException { // findPets may throw IOException
        // Setup
        String searchString = "la";
        Pet[] pets = new Pet[2];
        pets[0] = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        pets[1] = new Pet(99,"Greater sage-grouse", "Ice Gladiator");
        // When findPets is called with the search string, return the two
        /// pets above
        when(mockPetDAO.findPetsByType(searchString)).thenReturn(pets);

        // Invoke
        ResponseEntity<Pet[]> response = petController.searchPetsByType(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pets,response.getBody());
    }

    @Test
    public void testSearchPetsHandleException() throws IOException { // findPets may throw IOException
        // Setup
        String searchString = "an";
        // When createPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).findPets(searchString);

        // Invoke
        ResponseEntity<Pet[]> response = petController.searchPetsByName(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchPetsByTypeHandleException() throws IOException { // findPets may throw IOException
        // Setup
        String searchString = "an";
        // When createPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).findPetsByType(searchString);

        // Invoke
        ResponseEntity<Pet[]> response = petController.searchPetsByType(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeletePet() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // when deletePet is called return true, simulating successful deletion
        when(mockPetDAO.deletePet(petId)).thenReturn(true);

        // Invoke
        ResponseEntity<Pet> response = petController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeletePetNotFound() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // when deletePet is called return false, simulating failed deletion
        when(mockPetDAO.deletePet(petId)).thenReturn(false);

        // Invoke
        ResponseEntity<Pet> response = petController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeletePetHandleException() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // When deletePet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockPetDAO).deletePet(petId);

        // Invoke
        ResponseEntity<Pet> response = petController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
