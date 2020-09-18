package org.bee.fxgallery.db.model;

import java.io.InputStream;

/**
 * A class that represents a Bird table that is stored in 
 the Chinook DB.
 * 
 * @author Sleiman Rabah
 */
public class Bird {

    private int Id;
    private String scientificName;
    private String birdName;
    private InputStream imageInStream;
    private String category;

    public Bird() {
    }

    
    public Bird( String scientificName, String birnName, InputStream image, String category) {        
        this.scientificName = scientificName;
        this.birdName = birnName;
        this.imageInStream = image;
        this.category = category;
    }
    public Bird(int Id, String scientificName, String birnName, InputStream image, String category) {
        this.Id = Id;
        this.scientificName = scientificName;
        this.birdName = birnName;
        this.imageInStream = image;
        this.category = category;
    }

    
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getBirdName() {
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public InputStream getImageInStream() {
        return imageInStream;
    }

    public void setImageInStream(InputStream imageInStream) {
        this.imageInStream = imageInStream;
    }


 

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Bird{" + "Id=" + Id + ", scientificName=" + scientificName + ", birdName=" + birdName + ", category=" + category + '}';
    }  
    
    

}
