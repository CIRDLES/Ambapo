
package org.cirdles.ambapo;


import java.math.BigDecimal;
/**
 * Represents geographical coordinate.
 */
public class Coordinate {

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
    public Coordinate(final String longitude, final String latitude, final String datum) throws Exception {
       
        new Coordinate(new BigDecimal(longitude), new BigDecimal(latitude), datum);
    }

    /**
     * Creates coordinate object.
     *
     * @param longitude longitude
     * @param latitude latitude
     * @param datum datum
     * @throws java.lang.Exception
     */
    public Coordinate(final BigDecimal longitude, final BigDecimal latitude,
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
     * 
     * @param latitude 
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    /**
     * 
     * @param longitude 
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    
    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return String.format("(%f, %f)", longitude, latitude);
    }

    



}
