/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author evc1996
 */
public class UTMToLatLongTest {
    
    public UTMToLatLongTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of convert method, of class UTMToLatLong.
     * Positive Lat/Long
     * One-way Conversion
     */
    @Test
    public void testConvert() throws Exception {
        System.out.println("convert");
        
        BigDecimal easting = new BigDecimal(465005.3449);
        BigDecimal northing = new BigDecimal(9329005.2);
        char hemisphere = 'N';
        int zone = 48;
        char zoneLetter = 'X';
        
        UTM utm = new UTM(easting, northing, hemisphere, zone, zoneLetter);
        String datum = "WGS84";
        Coordinate expResult = new Coordinate(new BigDecimal(84.0), 
            new BigDecimal(102.0));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        assertEquals(expResult, result);

    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Positive Lat/Long
     * Two-way conversion
     */
    @Test
    public void testConvert2() throws Exception {
        System.out.println("convert");
        
        UTM utm = LatLongToUTM.convert(84.0, 102.0, "WGS84");
        
        String datum = "WGS84";
        Coordinate expResult = new Coordinate(new BigDecimal(84.0), 
            new BigDecimal(102.0));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Lat/Long
     * One-way conversion
     */
    @Test
    public void testConvert3() throws Exception {
        System.out.println("convert");
        
        BigDecimal easting = new BigDecimal(554084.381011733);
        BigDecimal northing = new BigDecimal(3236799.83581419);
        char hemisphere = 'S';
        int zone = 3;
        char zoneLetter = 'E';
        
        UTM utm = new UTM(easting, northing, hemisphere, zone, zoneLetter);
        String datum = "WGS84";
        
        Coordinate expResult = new Coordinate(new BigDecimal(-61),new BigDecimal(-164));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Lat/Long
     * Two-way conversion
     */
    @Test
    public void testConvert4() throws Exception {
        System.out.println("convert");
        
        UTM utm = LatLongToUTM.convert(-61.0, -164.0, "WGS84");
        
        String datum = "WGS84";
        Coordinate expResult = new Coordinate(new BigDecimal(-61.0),new BigDecimal(-164.0));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Lat
     * Positive Long
     * One-way conversion
     */
    @Test
    public void testConvert5() throws Exception {
        System.out.println("convert");
        
        BigDecimal easting = new BigDecimal(707889.216072834);
        BigDecimal northing = new BigDecimal(7676551.70980184);
        char hemisphere = 'S';
        int zone = 33;
        char zoneLetter = 'K';
        
        UTM utm = new UTM(easting, northing, hemisphere, zone, zoneLetter);
        String datum = "WGS84";
        
        Coordinate expResult = new Coordinate(new BigDecimal(-21),new BigDecimal(17));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Lat
     * Positive Long
     * Two-way conversion
     */
    @Test
    public void testConvert6() throws Exception {
        System.out.println("convert");
        
        UTM utm = LatLongToUTM.convert(-21.0, 17.0, "WGS84");
        
        String datum = "WGS84";
        Coordinate expResult = new Coordinate(new BigDecimal(-21.0),new BigDecimal(17.0));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Long
     * Positive Lat
     * One-way conversion
     */
    @Test
    public void testConvert7() throws Exception {
        System.out.println("convert");
        
        BigDecimal easting = new BigDecimal(500000);
        BigDecimal northing = new BigDecimal(8212038.09315851);
        char hemisphere = 'N';
        int zone = 1;
        char zoneLetter = 'X';
        
        UTM utm = new UTM(easting, northing, hemisphere, zone, zoneLetter);
        String datum = "WGS84";
        
        Coordinate expResult = new Coordinate(new BigDecimal(74),new BigDecimal(-177));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of convert method, of class UTMToLatLong.
     * Negative Long
     * Positive Lat
     * Two-way conversion
     */
    @Test
    public void testConvert8() throws Exception {
        System.out.println("convert");
        
        UTM utm = LatLongToUTM.convert(74, -177, "WGS84");
        
        String datum = "WGS84";
        Coordinate expResult = new Coordinate(new BigDecimal(74.0),new BigDecimal(-177.0));
        Coordinate result = UTMToLatLong.convert(utm, datum);
        
        result.setLatitude(result.getLatitude().setScale(0, RoundingMode.HALF_UP));
        result.setLongitude(result.getLongitude().setScale(0, RoundingMode.HALF_UP));
        
        assertEquals(expResult, result);
    }
}
