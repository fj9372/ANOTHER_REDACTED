package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ufund.api.ufundapi.model.Pet;
import com.ufund.api.ufundapi.model.User;

/**
 * Test the Basket File DAO class
 * 
 * @author Group 5E
 */
@Tag("Persistence-tier")
public class BasketFileDAOTest {
    BasketFileDAO basketFileDAO;
    Pet[] testPets;
    Pet[] testPetList;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void initEach() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class); 
        testPets = new Pet[4];
        testPetList = new Pet[5];
        testUsers = new User[2];
        testPets[0] = new Pet(99, "Lesser sage-grouse", "Wi-Fire");
        testPets[1] = new Pet(100, "Greater sage-grouse", "Galactic Agent");
        testPets[2] = new Pet(101, "Greater sage-grouse", "Ice Gladiator");
        testPets[3] = new Pet(103, "Greater sage-grouse", "T2");
        testPetList[0] = new Pet(99, "Lesser sage-grouse", "Wi-Fire");
        testPetList[1] = new Pet(100, "Greater sage-grouse", "Galactic Agent");
        testPetList[2] = new Pet(101, "Greater sage-grouse", "Ice Gladiator");
        testPetList[3] = new Pet(102, "Greater sage-grouse", "T1");
        testPetList[4] = new Pet(103, "Greater sage-grouse", "T2");
        testUsers[0] = new User("test", "test", new int[]{99, 102}, new int[]{100, 103}, new String[]{"Test Message"});
        testUsers[1] = new User("admin", "admin", new int[]{}, new int[]{}, new String[]{"Test Message"});
    // When the object mapper is supposed to read from the file
    // the mock object mapper will return the pet array above
        when(mockObjectMapper
        .readValue(new File("doesnt_matter.txt"),User[].class))
            .thenReturn(testUsers);
        when(mockObjectMapper
        .readValue(new File("doesnt_matter2.txt"),Pet[].class))
            .thenReturn(testPets);
        when(mockObjectMapper
        .readValue(new File("doesnt_matter3.txt"),Pet[].class))
            .thenReturn(testPetList);
        basketFileDAO = new BasketFileDAO("doesnt_matter.txt","doesnt_matter2.txt","doesnt_matter3.txt",mockObjectMapper);
        this.basketFileDAO.getPet("test");
    }

    @Test
    public void testGetPets() throws IOException {
        // Invoke
        Pet[] pets = basketFileDAO.getPet("test");
        String s ="";
        // Analyze
        assertEquals(1, pets.length);
        for (int i = 0; i < pets.length;i++){
            s+=pets[i].toString()+" ";
            //assertEquals(pets[i],testPets[i]);
        }
        System.out.println("YOP"+ s);
    }

    @Test
    public void testAdoptPet() throws IOException {
        boolean adopted = this.basketFileDAO.adoptPet(99);
        assertTrue(adopted);
    }

    @Test
    public void testAdoptPetFail() throws IOException {
        boolean adopted = this.basketFileDAO.adoptPet(101);
        assertFalse(adopted);
    }

    @Test
    public void testGetAdoptedPet() throws IOException{
        Pet[] adopted = this.basketFileDAO.getAdoptedPet("test");
        assertEquals(2, adopted.length);
    }

    @Test
    public void testGetPet() {
        // Invoke
        Pet pet = basketFileDAO.getPet(99);

        // Analzye
        assertEquals(testPets[0].getName(), pet.getName());
    }

    @Test
    public void testDeletePet() throws IOException {
        // Invoke

        boolean result = assertDoesNotThrow(() -> basketFileDAO.deletePet(99),
                            "Unexpected exception thrown");
        // Analzye
        assertEquals(true,result);
        // We check the internal tree map size against the length
        // of the test pets array - 1 (because of the delete)
        // Because pets attribute of basketFileDAO is package private
        // we can access it directly
        assertEquals(0,basketFileDAO.getPets().length);
    }

    @Test
    public void testCreatePet() {
        // Setup
        Pet pet = new Pet(110,"Greater sage-grouse", "Wonder-Person");
        
        // Invoke
        Pet result = assertDoesNotThrow(() -> basketFileDAO.createPet(pet),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Pet actual = basketFileDAO.getPet(pet.getId());
        assertEquals(pet.getId(),actual.getId());
        assertEquals(pet.getName(), actual.getName());
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        Pet pet = new Pet(110,"Greater sage-grouse","Wi-Fire");

        assertThrows(IOException.class,
                        () -> basketFileDAO.createPet(pet),
                        "IOException not thrown");
    }

    @Test
    public void testGetPetNotFound() {
        // Invoke
        Pet pet = basketFileDAO.getPet(98);

        // Analyze
        assertEquals(null,pet);
    }

    @Test
    public void testDeletePetNotFound() throws IOException {
        // Invoke
        this.basketFileDAO.getPet("test");
        boolean result = assertDoesNotThrow(() -> basketFileDAO.deletePet(98),
                                                "Unexpected exception thrown");
        Pet[] new_pets = this.basketFileDAO.getPet("test");
        // Analyze
        assertEquals(result,false);
        assertEquals(1,new_pets.length);
    }

    @Test
    public void testCreatePetFail() throws IOException{
        // Setup
        Pet pet = new Pet(99,"Lesser sage-grouse", "Wi-Fire");
        
        // Invoke
        Pet result = assertDoesNotThrow(() -> basketFileDAO.createPet(pet),
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
        // from the basketFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter2.txt"),Pet[].class);
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter3.txt"),Pet[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new BasketFileDAO("doesnt_matter.txt", "doesnt_matter2.txt", "doesnt_matter3.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
