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
            char zoneLetter) {
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
