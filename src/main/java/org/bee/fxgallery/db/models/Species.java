/*
 * Copyright (C) 2020 frostybee.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bee.fxgallery.db.models;

/**
 * A class that represents a species table that is stored in the SQLite DB.
 *
 *
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
