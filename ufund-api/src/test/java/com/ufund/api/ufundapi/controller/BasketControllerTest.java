package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Pet;
import com.ufund.api.ufundapi.persistence.BasketDAO;
/**
 * Test the Basket Controller class
 * 
 * @author Group 5E
 */
@Tag("Controller-tier")
public class BasketControllerTest {
   private BasketController basketController;
    private BasketDAO mockBasketDAO;

    /**
     * Before each test, create a new basketController object and inject
     * a mock Pet DAO
     */
    @BeforeEach
    public void setupbasketController() {
        mockBasketDAO = mock(BasketDAO.class);
        basketController = new BasketController(mockBasketDAO);
    }   

    @Test
    public void testGetPet() throws IOException {  // getPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Galactic Agent");
        // When the same id is passed in, our mock Pet DAO will return the Pet object
        when(mockBasketDAO.getPet(pet.getId())).thenReturn(pet);

        // Invoke
        ResponseEntity<Pet> response = basketController.getPet(pet.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pet,response.getBody());
    }

    @Test
    public void testGetAdoptedPet() throws IOException{
        ResponseEntity<Pet[]> response = basketController.getAdoptedPet("test");
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testGetAdoptedPetError() throws IOException{
        doThrow(new IOException()).when(mockBasketDAO).getAdoptedPet("test");
        ResponseEntity<Pet[]> response = basketController.getAdoptedPet("test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetPetWithUsername() throws IOException{
        ResponseEntity<Pet[]> response = basketController.getPet("test");
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testGetPetWithUsernameError() throws IOException{
        doThrow(new IOException()).when(mockBasketDAO).getPet("test");
        ResponseEntity<Pet[]> response = basketController.getPet("test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testAdoptPet() throws IOException{
        int petId = 99;
        when(mockBasketDAO.adoptPet(petId)).thenReturn(true);
        ResponseEntity<Pet> response = basketController.adoptPet(petId);
        when(mockBasketDAO.adoptPet(petId)).thenReturn(false);
        ResponseEntity<Pet> response2 = basketController.adoptPet(petId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(HttpStatus.CONFLICT,response2.getStatusCode());
    }

    @Test
    public void testAdoptPetException() throws IOException{
        int petId = 99;
        doThrow(new IOException()).when(mockBasketDAO).adoptPet(petId);
        ResponseEntity<Pet> response = basketController.adoptPet(petId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    @Test
    public void testGetPetNotFound() throws Exception { // createPet may throw IOException
        // Setup
        int petId = 99;
        // When the same id is passed in, our mock Pet DAO will return null, simulating
        // no pet found
        when(mockBasketDAO.getPet(petId)).thenReturn(null);

        // Invoke
        ResponseEntity<Pet> response = basketController.getPet(petId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetPetHandleException() throws Exception { // createPet may throw IOException
        // Setup
        int petId = 99;
        // When getPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).getPet(petId);

        // Invoke
        ResponseEntity<Pet> response = basketController.getPet(petId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all basketController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreatePet() throws IOException {  // createPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Wi-Fire");
        // when createPet is called, return true simulating successful
        // creation and save
       when(mockBasketDAO.createPet(pet)).thenReturn(pet);

        // Invoke
        ResponseEntity<Pet> response = basketController.createPet(pet);

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
        when(mockBasketDAO.createPet(pet)).thenReturn(null);

        // Invoke
        ResponseEntity<Pet> response = basketController.createPet(pet);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreatePetHandleException() throws IOException {  // createPet may throw IOException
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse", "Ice Gladiator");

        // When createPet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).createPet(pet);

        // Invoke
        ResponseEntity<Pet> response = basketController.createPet(pet);

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
        when(mockBasketDAO.getPets()).thenReturn(pets);

        // Invoke
        ResponseEntity<Pet[]> response = basketController.getPets();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(pets,response.getBody());
    }

    @Test
    public void testGetPetsHandleException() throws IOException { // getPets may throw IOException
        // Setup
        // When getPets is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).getPets();

        // Invoke
        ResponseEntity<Pet[]> response = basketController.getPets();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeletePet() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // when deletePet is called return true, simulating successful deletion
        when(mockBasketDAO.deletePet(petId)).thenReturn(true);

        // Invoke
        ResponseEntity<Pet> response = basketController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeletePetNotFound() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // when deletePet is called return false, simulating failed deletion
        when(mockBasketDAO.deletePet(petId)).thenReturn(false);

        // Invoke
        ResponseEntity<Pet> response = basketController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeletePetHandleException() throws IOException { // deletePet may throw IOException
        // Setup
        int petId = 99;
        // When deletePet is called on the Mock Pet DAO, throw an IOException
        doThrow(new IOException()).when(mockBasketDAO).deletePet(petId);

        // Invoke
        ResponseEntity<Pet> response = basketController.deletePet(petId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    } 
}
