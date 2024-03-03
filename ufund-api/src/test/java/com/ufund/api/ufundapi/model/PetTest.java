package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Pet class
 * 
 * @author Group 5E
 */
@Tag("Model-tier")
public class PetTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Bob";
        String expected_type = "Greater sage-grouse";

        // Invoke
        Pet pet = new Pet(expected_id,expected_type, expected_name);

        // Analyze
        assertEquals(expected_id,pet.getId());
        assertEquals(expected_type,pet.getAnimaltype());
        assertEquals(expected_name,pet.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Bob";
        String type = "Greater sage-grouse";
        Pet pet = new Pet(id,type, name);

        String expected_name = "Bin";

        // Invoke
        pet.setName(expected_name);

        // Analyze
        assertEquals(expected_name,pet.getName());
    }
    @Test
    public void testType(){
        //Setup
        int id =99;
        String name = "Bob";
        String type = "Greater sage-grouse";
        Pet pet = new Pet(id,type, name);

        String expected_type = "Great Horned Owl";
        //Invoke
        pet.setAnimaltype(expected_type);
        //Analyze
        assertEquals(expected_type,pet.getAnimaltype());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String type = "Greater sage-grouse";
        String name = "Wi-Fire";
        String expected_string = String.format(Pet.STRING_FORMAT,id,type,name);
        Pet pet = new Pet(id,type, name);

        // Invoke
        String actual_string = pet.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}