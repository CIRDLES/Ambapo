/*
 * UTM.java
 *
 * Created Oct 25, 2015
 *
 * Copyright 2015 CIRDLES.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cirdles.ambapo;

import java.math.BigDecimal;

/**
 *
 * @author Elaina Cole
 */
public class UTM {

    
    private char hemisphere;
    private int zoneNumber;
    private BigDecimal easting;
    private BigDecimal northing;
    private char zoneLetter;
    
    public static final Character[] ZONE_LETTERS = new Character[] {'*', 'C', 'D', 
        'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 
        'V', 'W', 'X'};
    
    public static final Integer[] ZONE_NUMBERS = new Integer[] {1, 2, 3, 4, 5, 6, 
        7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 
        44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
    
    public static final int MIN_EASTING = 0;
    public static final int MAX_EASTING = 1000000;
    
    public static final int MIN_NORTHING = 0;
    public static final int MAX_NORTHING = 10000000;
    
    /**
     * If you do not know the hemisphere or zone letter, denote it with an asterisk,
     * but you must know one or the other. 
     * @param easting
     * @param northing
     * @param hemisphere
     * @param zoneNumber
     * @param zoneLetter
     * @throws Exception 
     */
    public UTM(BigDecimal easting, BigDecimal northing, char hemisphere, int zoneNumber,
            char zoneLetter) throws Exception{
        
        if(hemisphere == '*' && zoneLetter == '*') {
            throw new Exception("You must have either a hemisphere or a zoneLetter");
        }
        
        setHemisphereIfNull();
        
        zoneLetter = Character.toUpperCase(zoneLetter);
        hemisphere = Character.toUpperCase(hemisphere);
        
        if (easting.compareTo(BigDecimal.ZERO) < 0 || northing.compareTo(
            BigDecimal.ZERO) < 0) {
            throw new Exception("Easting and Northing must be >= ZERO.");
        }
        
        if(zoneNumber < 1 || zoneNumber > 60)
            throw new Exception("Zone number must fall in the range from 1 to 60.");
        
        if((Character.isLetter(zoneLetter) == false && zoneLetter != '*') || zoneLetter == 'A' ||
            zoneLetter == 'B' || zoneLetter == 'I' || zoneLetter == 'O' || 
            zoneLetter == 'Y' || zoneLetter == 'Z') {
            
            throw new Exception("Zone letter must be a letter in the alphabet, "
                    + "except A, B, I, O, Y, and Z and yours was " + zoneLetter);
        
        }
        
        
        this.easting = easting;
        this.northing = northing;
        this.hemisphere = hemisphere;
        this.zoneNumber = zoneNumber;
        this.zoneLetter = zoneLetter;
    }
    
    /**
     * 
     * @return easting
     */
    public BigDecimal getEasting() {
        return easting;  
    }
    
    /**
     * 
     * @return northing
     */
    public BigDecimal getNorthing() {
        return northing;
    }
    
    /**
     * 
     * @return zone number
     */
    public int getZoneNumber() {
        return zoneNumber;
    }
    
    /**
     * 
     * @return zone letter
     */
    public char getZoneLetter() {
        return zoneLetter;
    }
    
    /**
     * 
     * @return hemisphere
     */
    public char getHemisphere() {
        setHemisphereIfNull();
        return hemisphere;
    }
    
    private void setHemisphereIfNull() {
        if(hemisphere == '*') {
            if((int)zoneLetter >= 80)
                this.hemisphere = 'N';
            else
                this.hemisphere = 'S';  
        }
    }
}
