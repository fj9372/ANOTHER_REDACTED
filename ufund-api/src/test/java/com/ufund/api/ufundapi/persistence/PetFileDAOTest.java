package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Pet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Pet File DAO class
 * 
 * @author Group 5E
 */
@Tag("Persistence-tier")
public class PetFileDAOTest {
    PetFileDAO petFileDAO;
    Pet[] testPets;
    Pet[] testPetList;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void initEach() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testPets = new Pet[3];
        testPetList = new Pet[3];
        testPets[0] = new Pet(99,"Lesser sage-grouse", "Wi-Fire");
        testPets[1] = new Pet(100,"Greater sage-grouse", "Galactic Agent");
        testPets[2] = new Pet(101,"Greater sage-grouse", "Ice Gladiator");
        testPetList[0] = new Pet(99,"Lesser sage-grouse", "Wi-Fire");
        testPetList[1] = new Pet(100,"Greater sage-grouse", "Galactic Agent");
        testPetList[2] = new Pet(101,"Greater sage-grouse", "Ice Gladiator");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the pet array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Pet[].class))
                .thenReturn(testPets);
        when(mockObjectMapper
            .readValue(new File("doesnt_matter2.txt"),Pet[].class))
                .thenReturn(testPetList);
        petFileDAO = new PetFileDAO("doesnt_matter.txt","doesnt_matter2.txt",mockObjectMapper);
         Map<Integer,Pet> pets = new HashMap<Integer,Pet>();
        pets.put(99, testPets[0]);
        pets.put(100,testPets[1]);
        pets.put(101,testPets[2]);
        petFileDAO.setPets(pets);
    }

    @Test
    public void testGetPets() {
        // Invoke
        Pet[] pets = petFileDAO.getPets();

        // Analyze
        assertEquals(pets.length,testPets.length);
        for (int i = 0; i < testPets.length;++i)
            assertEquals(pets[i],testPets[i]);
    }

    @Test
    public void testFindPets() {
        // Invoke
        Pet[] pets = petFileDAO.findPets("wi");

        // Analyze
        assertEquals(pets.length,1);
        assertEquals(pets[0],testPets[0]);
    }

    @Test
    public void testFindPetsByType() {
        // Invoke
        Pet[] pets = petFileDAO.findPetsByType("le");

        // Analyze
        assertEquals(pets.length,1);
        assertEquals(pets[0],testPets[0]);
    }

    @Test
    public void testGetPet() {
        // Invoke
        Pet pet = petFileDAO.getPet(99);

        // Analzye
        assertEquals(pet.getName(),testPets[0].getName());
    }

    @Test
    public void testDeletePet() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> petFileDAO.deletePet(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test pets array - 1 (because of the delete)
        // Because pets attribute of PetFileDAO is package private
        // we can access it directly
        assertEquals(petFileDAO.pets.size(),testPets.length-1);
    }

    @Test
    public void testCreatePet() {
        // Setup
        Pet pet = new Pet(102,"Greater sage-grouse", "Wonder-Person");

        // Invoke
        Pet result = assertDoesNotThrow(() -> petFileDAO.createPet(pet),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        assertEquals(result.getId(),pet.getId());
        assertEquals(result.getName(),pet.getName());
    }

    @Test
    public void testUpdatePet() {
        // Setup
        Pet pet = new Pet(99,"Greater sage-grouse","Galactic Agent");

        // Invoke
        Pet result = assertDoesNotThrow(() -> petFileDAO.updatePet(pet),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Pet actual = petFileDAO.getPet(pet.getId());
        assertEquals(actual,pet);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Pet[].class));

        Pet pet = new Pet(102,"Greater sage-grouse","Wi-Fire");

        assertThrows(IOException.class,
                        () -> petFileDAO.createPet(pet),
                        "IOException not thrown");
    }

    @Test
    public void testGetPetNotFound() {
        // Invoke
        Pet pet = petFileDAO.getPet(98);

        // Analyze
        assertEquals(pet,null);
    }

    @Test
    public void testDeletePetNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> petFileDAO.deletePet(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(petFileDAO.pets.size(),testPets.length);
    }

    @Test
    public void testUpdatePetNotFound() {
        // Setup
        Pet pet = new Pet(98,"Greater sage-grouse","Bolt");

        // Invoke
        Pet result = assertDoesNotThrow(() -> petFileDAO.updatePet(pet),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the PetFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Pet[].class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt2"),Pet[].class);
        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new PetFileDAO("doesnt_matter.txt", "doesnt_matter2.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
