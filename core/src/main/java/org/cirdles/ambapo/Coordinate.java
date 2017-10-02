/*
 * Copyright 2006-2017 CIRDLES.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cirdles.ambapo;


import java.math.BigDecimal;
/**
 * Represents geographical coordinate.
 */
public class Coordinate {
    public static final int MAX_LONGITUDE = 180;
    public static final int MIN_LONGITUDE = -180;
    
    public static final int MAX_LATITUDE = 90;
    public static final int MIN_LATITUDE = -90;

    /**
     * Longitude.
     */
    private BigDecimal longitude;
    /**
     * Latitude.
     */
    private BigDecimal latitude;
    
    /**
     * Datum.
     */
    private Datum datum;

    /**
     * Creates coordinate object.
     *
     * @param longitude longitude
     * @param latitude latitude
     * @param datum datum
     * @throws java.lang.Exception;
     */
    public Coordinate(final String latitude, final String longitude, final String datum) throws Exception {
       
        this(new BigDecimal(longitude), new BigDecimal(latitude), datum);
    }

    /**
     * Creates coordinate object.
     *
     * @param longitude longitude
     * @param latitude latitude
     * @param datum datum
     * @throws java.lang.Exception
     */
    public Coordinate(final BigDecimal latitude, final BigDecimal longitude,
            String datum) throws Exception{
        
        if (longitude.compareTo(new BigDecimal(180)) > 0 || longitude.compareTo(
            new BigDecimal(-180)) < 0) {
            
            throw new Exception("Longitude must be at or between -180 and 180"
                    + "degrees");
            
        }
        
        if(latitude.compareTo(new BigDecimal(-90)) < 0 || latitude.compareTo(new
            BigDecimal(90)) > 0) {
            
            throw new Exception("Latitude must be at or between 0 and 90 degrees");
            
        }
        
        if (!Datum.containsDatum(datum))
            throw new Exception("Datum doesn't exist.");
        
        this.latitude = latitude;
        this.longitude = longitude;
        this.datum = Datum.valueOf(datum);
    }

    /**
     * Returns longitude.
     *
     * @return longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }


    /**
     * Returns latitude.
     *
     * @return latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    /**
     * Set the latitude.
     * @param latitude 
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    /**
     * Set the longitude
     * @param longitude 
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    
    /**
     * Returns Coordinate in a string format of "(latitude, longitude)."
     * @return String
     */
    @Override
    public String toString() {
        return String.format("(%f, %f)", latitude, longitude);
    }
    
    /**
     * Returns if Coordinate objects are equal.
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        
        if(!((obj) instanceof Coordinate))
            return false;
        
        boolean isEqual = true;
        
        if (((Coordinate)obj).getLatitude().compareTo(this.getLatitude()) != 0)
            isEqual = false;
        if (((Coordinate)obj).getLongitude().compareTo(this.getLongitude()) != 0)
            isEqual = false;
        if (((Coordinate)obj).getDatum().compareTo(this.getDatum()) != 0)
            isEqual = false;
        
        return isEqual;
        
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get the datum of the specific Coordinate.
     * @return the datum
     */
    public Datum getDatum() {
        return datum;
    }

    /**
     * Set the datum of a particular Coordinate.
     * @param datum the datum to set
     */
    public void setDatum(Datum datum) {
        this.datum = datum;
    }
    



}
