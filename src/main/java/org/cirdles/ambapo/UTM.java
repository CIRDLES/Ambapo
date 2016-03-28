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
    private int zone;
    private BigDecimal easting;
    private BigDecimal northing;
    private char zoneLetter;
    
    public UTM(BigDecimal easting, BigDecimal northing, char hemisphere, int zone,
            char zoneLetter) throws Exception{
        
        zoneLetter = Character.toUpperCase(zoneLetter);
        hemisphere = Character.toUpperCase(hemisphere);
        
        if (easting.compareTo(BigDecimal.ZERO) < 0 || northing.compareTo(
            BigDecimal.ZERO) < 0) {
            throw new Exception("Easting and Northing must be >= ZERO.");
        }
        
        if(zone < 1 || zone > 60)
            throw new Exception("Zone number must fall in the range from 1 to 60.");
        
        if(Character.isLetter(zoneLetter) == false || zoneLetter == 'A' ||
            zoneLetter == 'B' || zoneLetter == 'I' || zoneLetter == 'O' || 
            zoneLetter == 'Y' || zoneLetter == 'Z') {
            
            throw new Exception("Zone letter must be a letter in the alphabet, "
                    + "except A, B, I, O, Y, and Z");
        
        }
        
        
        this.easting = easting;
        this.northing = northing;
        this.hemisphere = hemisphere;
        this.zone = zone;
        this.zoneLetter = zoneLetter;
    }
    
    public BigDecimal getEasting() {
        return easting;  
    }
    
    public BigDecimal getNorthing() {
        return northing;
    }
    
    public int getZoneNumber() {
        return zone;
    }
    
    public char getZoneLetter() {
        return zoneLetter;
    }
    
    public char getHemisphere() {
        return hemisphere;
    }
}
