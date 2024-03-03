package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Pet(need) entity
 *
 */
public class Pet {

    // Package private for tests
    static final String STRING_FORMAT = "Pet [id=%d, animaltype=%s, name=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("animaltype") private String animaltype;
    @JsonProperty("name") private String name;

    /**
     * Create a Pet with the given id, animaltype and name
     * @param id The id of the pet
     * @param animaltype The type of animal the pet is
     * @param name The name of the pet
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Pet(@JsonProperty("id") int id, @JsonProperty("animaltype") String animaltype, @JsonProperty("name") String name) {
        this.id = id;
        this.animaltype = animaltype;
        this.name = name;
    }

    /**
     * Retrieves the id of the pet
     * @return The id of the pet
     */
    public int getId() {return id;}

    /**
     * Sets the animal type of the pet. 
     * @param animaltype
     */
    public void setAnimaltype(String animaltype) {this.animaltype= animaltype;}

    /**
     * Retrieves the type of animal the pet is.
     * @return The type of animal pet is
     */
    public String getAnimaltype(){return animaltype;}

    /**
     * Sets the name of the pet 
     * @param name The name of the pet
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the pet
     * @return The name of the pet
     */
    public String getName() {return name;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,animaltype,name);
    }
}