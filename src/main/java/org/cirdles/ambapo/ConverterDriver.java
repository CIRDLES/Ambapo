/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import java.io.File;
import java.math.BigDecimal;

/**
 *
 * @author Elaina Cole
 */
public class ConverterDriver {
    
    public static void main(String[] args) throws Exception {
        
        BigDecimal latitude = new BigDecimal(84);
        BigDecimal longitude = new BigDecimal(102);
        
        UTM utm = LatLongToUTM.convert(latitude, longitude, "WGS84");
        System.out.println("Northing: " + utm.getNorthing());
        System.out.println("Easting: " + utm.getEasting());
        System.out.println("Zone: " + utm.getZoneNumber() + " " + utm.getZoneLetter());
        System.out.println("Hemisphere: " + utm.getHemisphere());
        
        double lat = -1.0;
        double lng = -77.0;
        
        UTM utm2 = LatLongToUTM.convert(lat, lng, "NAD83");
        System.out.println("Northing: " + utm2.getNorthing());
        System.out.println("Easting: " + utm2.getEasting());
        System.out.println("Zone: " + utm2.getZoneNumber() + " " + utm2.getZoneLetter());
        System.out.println("Hemisphere: " + utm2.getHemisphere());
        
        
        UTM utm3 = new UTM(new BigDecimal(277438.2635456444), new BigDecimal(9889402.0265816805),
            'S', 18, 'M');
        
        Coordinate latLong = org.cirdles.ambapo.UTMToLatLong.convert(utm3, "WGS84");
        
        System.out.println(latLong);
        
        UTM utm4 = new UTM(new BigDecimal(500000.9999), new BigDecimal(499961.8279),
            'N', 25, 'N');
        
        Coordinate latLong2 = UTMToLatLong.convert(utm4, "WGS84");
        System.out.println ("\nCoordinate=" + latLong2);
        
        System.out.println("\nBulk convert UTM to Lat Long");
        File file = new File("utmToLatLongBulk.csv");
        UTMToLatLong.bulkConvert(file);
        
        System.out.println("\nBulk convert Lat Long to UTM");
        File file2 = new File("outputLatLong.csv");
        LatLongToUTM.bulkConvert("outputLatLong.csv", "outputUTM.csv");
        
        
    }
    
}
