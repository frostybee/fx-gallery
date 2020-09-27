package org.bee.fxgallery.db.model;


/**
 * A class that represents a species table that is stored in the SQLite DB.
 *
 * @author Sleiman Rabah
 */
public class Species {

    private int Id;
    private String scientificName;
    private String commonName;
    private byte[] imageBytes;
    private String family;

    public Species() {
    }

    public Species(String scientificName, String commonName, byte[] bytesArray, String family) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.imageBytes = bytesArray;
        this.family = family;
    }

    public Species(int Id, String scientificName, String commonName, byte[] bytesArray, String family) {
        this.Id = Id;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.imageBytes = bytesArray;
        this.family = family;
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

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String category) {
        this.family = category;
    }

    @Override
    public String toString() {
        return "Species{" + "Id=" + Id + ", scientificName=" + scientificName + ", commonName=" + commonName + ", imageBytes=" + imageBytes + ", category=" + family + '}';
    }

    

}
