
/*
 * LatLongToUTM.java
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
 

The formulas are based on Charles Karney's formulas for UTM conversion:
"Transverse Mercator with an accuracy of a few nanometers"
Charles F. F. Karney
SRI International, 201 Washington Rd, Princeton, NJ 08543-5300

Information that helped me understand the formulas:
"How to Use the Spreadsheet for Converting UTM to Latitude and Longitude (Or Vice Versa)" and
"Converting UTM to Latitude and Longitude (Or Vice Versa)"
Steven Dutch, Natural and Applied Sciences, University of Wisconsin - Green Bay

 */
package org.cirdles.ambapo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;


/**
 * Converts latitude and longitude coordinates to UTM.
 * @author Elaina Cole
 */
public class LatLongToUTM {
    
    private static final BigDecimal SCALE_FACTOR = new BigDecimal(0.9996);
    private static final BigDecimal FALSE_EASTING = new BigDecimal(500000);
    private static final BigDecimal SOUTH_HEMISPHERE_SUBTRACTION = new BigDecimal(10000000);
    private static final int PRECISION = 9;
    
    
    /**
     * Converts double latitude longitude to BigDecimal and converts it to UTM
     * 
     * @param latitude
     * @param longitude
     * @param datumName
     * @return UTM
     * @throws java.lang.Exception
     * 
     * 
     */
    
    public static UTM convert(double latitude, double longitude, String datumName) throws Exception {
        
        return convert(new BigDecimal(latitude), new BigDecimal(longitude), datumName);
        
    }     
    
    /**
     * Converts BigDecimal latitude longitude to UTM 
     * 
     * @param latitude
     * @param longitude
     * @param datumName
     * @return UTM
     * @throws java.lang.Exception
     * 
     * 
     */
    public static UTM convert(BigDecimal latitude, BigDecimal longitude, String datumName) throws Exception{
        
        Datum datum = Datum.valueOf(datumName);
        
        BigDecimal meridianRadius = new BigDecimal(datum.getMeridianRadius());
        BigDecimal eccentricity = new BigDecimal(datum.getEccentricity());
        
        BigDecimal latitudeRadians = latitude.abs().multiply(
                new BigDecimal(Math.PI)).divide(new BigDecimal(180.0), PRECISION,
                RoundingMode.HALF_UP);
        
        int zoneNumber = calcZoneNumber(longitude);
        
        BigDecimal zoneCentralMeridian = calcZoneCentralMeridian(zoneNumber);
        
        BigDecimal changeInLongitudeDegree = (longitude.subtract(zoneCentralMeridian)
                ).abs().setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal changeInLongitudeRadians = (changeInLongitudeDegree.multiply(
                new BigDecimal(Math.PI))).divide(new BigDecimal(180), PRECISION, 
                RoundingMode.HALF_UP);
        

        BigDecimal conformalLatitude = calcConformalLatitude(eccentricity, 
                latitudeRadians).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal tauPrime = (new BigDecimal(Math.tan(conformalLatitude.
                doubleValue()))).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal xiPrimeNorth = calcXiPrimeNorth(changeInLongitudeRadians, 
                tauPrime).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal etaPrimeEast = calcEtaPrimeEast(changeInLongitudeRadians, 
                tauPrime).setScale(PRECISION, RoundingMode.HALF_UP);
        
        double[] alphaSeries = datum.getAlphaSeries();
        

        BigDecimal xiNorth = calcXiNorth(xiPrimeNorth, etaPrimeEast, 
                alphaSeries).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal etaEast = calcEtaEast(xiPrimeNorth, etaPrimeEast, 
                alphaSeries).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal easting = calcEasting(meridianRadius, etaEast, longitude, 
                zoneCentralMeridian).setScale(PRECISION, RoundingMode.HALF_UP);
        BigDecimal northing = calcNorthing(meridianRadius, xiNorth, 
                latitude).setScale(PRECISION, RoundingMode.HALF_UP);
        
        char zoneLetter = calcZoneLetter(latitude);
        char hemisphere = calcHemisphere(latitude);
        
        UTM utm = new UTM(easting.setScale(5, RoundingMode.HALF_UP), northing, hemisphere, zoneNumber, zoneLetter);
        
        return utm;
    }
    

    
    /**
     * Returns the zone number that the longitude corresponds to on the UTM map
     * 
     * @param longitude
     * @return int zone number
     * 
     * 
     */
    private static int calcZoneNumber(BigDecimal longitude) {
        int zoneNumber;
        BigDecimal six = new BigDecimal(6);
        
        if (longitude.signum() < 0) {
            
            BigDecimal oneEighty = new BigDecimal(180);
            zoneNumber = ((oneEighty.add(longitude)).divide(six, PRECISION, 
                    RoundingMode.HALF_UP)).intValue()+ 1;
        }
            
        else {
            
            BigDecimal thirtyOne = new BigDecimal(31);
            zoneNumber = ((longitude.divide(six, PRECISION, RoundingMode.HALF_UP)).abs().add(
                    thirtyOne)).intValue();
        }
        
        return zoneNumber;
        
    }
    
    /**
     * Central meridian is the center of the zone on the UTM map
     * 
     * @param zoneNumber
     * @return BigDecimal central meridian
     * 
     * 
     */
    private static BigDecimal calcZoneCentralMeridian(int zoneNumber) {
        
        BigDecimal zoneCentralMeridian = new BigDecimal(zoneNumber * 6 - 183);
        return zoneCentralMeridian;
    
    }
    
    /**
     * Eccentricity helps define the shape of the ellipsoidal representation of
     * the earth
     * 
     * Conformal latitude gives an angle-preserving (conformal) transformation 
     * to the sphere
     * 
     * It defines a transformation from the ellipsoid to a sphere 
     * of arbitrary radius such that the angle of intersection between any two 
     * lines on the ellipsoid is the same as the corresponding angle on the sphere.
     * 
     * @param eccentricity
     * @param latitudeRadians
     * @return BigDecimal conformal latitude
     * 
     
     */
    private static BigDecimal calcConformalLatitude(BigDecimal eccentricity, BigDecimal latitudeRadians) {
        
        BigDecimal conformalLatitude;
        
        double latRadDouble = latitudeRadians.doubleValue();
        double eccDouble = eccentricity.doubleValue();
        double confLatDouble;
        
        Atanh atanh = new Atanh();
        Asinh asinh = new Asinh();
        
        confLatDouble = Math.atan(Math.sinh(asinh.value( Math.tan(latRadDouble)) -
            eccDouble * atanh.value(eccDouble * Math.sin(latRadDouble))));
        
        conformalLatitude = new BigDecimal(confLatDouble);
        
        return conformalLatitude;
        
    }
    

    /**
     * tau refers to the torsion of a curve
     * xi refers to the north-south direction
     * 
     * @param changeInLongitudeRadians
     * @param tauPrime
     * @return BigDecimal xi prime
     * 
     * 
     */
    private static BigDecimal calcXiPrimeNorth(BigDecimal changeInLongitudeRadians,
            BigDecimal tauPrime) {
        
        double cosOfLatRad = Math.cos(changeInLongitudeRadians.doubleValue());
        
        BigDecimal xiPrime = new BigDecimal(Math.atan(tauPrime.doubleValue() / cosOfLatRad));
        
        return xiPrime;
    }
    
    /**
     * eta refers to the east-west direction
     * 
     * @param changeInLongitudeRadians
     * @param tauPrime
     * @return BigDecimal eta prime
     * 
     * 
     */
    private static BigDecimal calcEtaPrimeEast(BigDecimal changeInLongitudeRadians, 
            BigDecimal tauPrime) {
        
        BigDecimal etaPrime;
        
        double sinOfLatRad = Math.sin(changeInLongitudeRadians.doubleValue());
        double cosOfLatRad = Math.cos(changeInLongitudeRadians.doubleValue());
        double cosOfLatRadSquared = Math.pow(cosOfLatRad, 2);
        
        BigDecimal tauPrimeSquared = tauPrime.pow(2);
        
        double sqrt = Math.sqrt(tauPrimeSquared.doubleValue() + cosOfLatRadSquared);
        double sinOverSqrt = sinOfLatRad / sqrt;
        
        Asinh asinhOfSin = new Asinh();
        etaPrime = new BigDecimal(asinhOfSin.value(sinOverSqrt));
        
        return etaPrime;
        
    }
    
    
    /**
     * alpha series based on Krüger series, which entails mapping the ellipsoid 
     * to the conformal sphere.
     * @param xiPrimeNorth
     * @param etaPrimeEast
     * @param alphaSeries
     * @return BigDecimal xi
     * 
     */
    private static BigDecimal calcXiNorth(BigDecimal xiPrimeNorth,
            BigDecimal etaPrimeEast, double[] alphaSeries) {

        double multiplicand = 2;
        double xiNorthDouble = xiPrimeNorth.doubleValue();
        double xiPrimeNortDouble = xiPrimeNorth.doubleValue();
        double etaPrimeEastDouble = etaPrimeEast.doubleValue();
        
        for (double alpha : alphaSeries) {
            
            double sinOfXiPrimeNorth = Math.sin(
                    xiPrimeNortDouble * multiplicand);
            
            double coshOfEtaPrimeEast = Math.cosh(
                    etaPrimeEastDouble * multiplicand);
            
            double augend = (alpha * sinOfXiPrimeNorth) * 
                    coshOfEtaPrimeEast;
            
            xiNorthDouble = xiNorthDouble + augend;
            
            multiplicand = multiplicand + 2;
        }
        
        BigDecimal xiNorth = new BigDecimal(xiNorthDouble);
        
        return xiNorth;
       
        
    }
    
    /**
     * 
     * @param xiPrimeNorth
     * @param etaPrimeEast
     * @param alphaSeries
     * @return BigDecimal eta
     */
    private static BigDecimal calcEtaEast(BigDecimal xiPrimeNorth,BigDecimal 
            etaPrimeEast, double[] alphaSeries) {
        
        
        double multiplicand = 2;
        double etaEastDouble = etaPrimeEast.doubleValue();
        double etaPrimeEastDouble = etaPrimeEast.doubleValue();
        double xiPrimeNorthDouble = xiPrimeNorth.doubleValue();
        
        for(int i=0; i < alphaSeries.length - 2; i++) {
            
            double cosOfXiPrimeNorth = Math.cos(
                    xiPrimeNorthDouble * multiplicand);
            
            double sinhOfEtaPrimeEast = Math.sinh(
                    etaPrimeEastDouble * multiplicand);
            
            double augend = (alphaSeries[i] * cosOfXiPrimeNorth)*
                    sinhOfEtaPrimeEast;
            
            etaEastDouble = etaEastDouble + augend;
            multiplicand = multiplicand + 2;
            
        }
        
        double cosOfXiPrimeNorth = Math.cos(
                    xiPrimeNorthDouble * multiplicand);
            
        double sinhOfEtaPrimeEast = Math.sinh(
                    etaPrimeEastDouble * multiplicand);
            
        double augend = (alphaSeries[5] * cosOfXiPrimeNorth)*
                    sinhOfEtaPrimeEast;
            
        etaEastDouble = etaEastDouble + augend;
        
        BigDecimal etaEast = new BigDecimal(etaEastDouble);
        
        return etaEast;

        
    }
    
    /**
     * Latitude corresponds to a zone letter on the UTM map. Match up zone letter
     * and zone number on UTM map to find the specific zone.
     * @param latitude
     * @return char zone letter
     */
    private static char calcZoneLetter(BigDecimal latitude) {
        String letters = "CDEFGHJKLMNPQRSTUVWXX";
        double lat = latitude.doubleValue();
        
        if(lat >= -80 && lat <= 84)
            return letters.charAt(new Double(Math.floor((lat+80.0)/8.0)).intValue());
        
        else
            return 'X';
        
    } 

    /**
     * The meridian radius is based on the lines that run north-south on a map
     * UTM easting coordinates are referenced to the center line of the zone 
     * known as the central meridian
     * The central meridian is assigned an 
     * easting value of 500,000 meters East.
     * @param meridianRadius
     * @param etaEast
     * @param longitude
     * @param centralMeridian
     * @return BigDecimal easting
     * 
     */
    private static BigDecimal calcEasting(BigDecimal meridianRadius, BigDecimal
            etaEast, BigDecimal longitude, BigDecimal centralMeridian) { 
        
        BigDecimal easting = (SCALE_FACTOR.multiply(meridianRadius)).multiply(etaEast);
        BigDecimal eastOfCM = BigDecimal.ONE;
        
        if (longitude.compareTo(centralMeridian) < 0)
            eastOfCM = eastOfCM.multiply(new BigDecimal(-1));
        
        easting = FALSE_EASTING.add(eastOfCM.multiply(easting));
        
        
        return easting;
    }
    
    /**
     * UTM northing coordinates are measured relative to the equator
     * For locations north of the equator the equator is assigned the northing 
     * value of 0 meters North
     * To avoid negative numbers, locations south of the equator are made with 
     * the equator assigned a value of 10,000,000 meters North.
     * @param meridianRadius
     * @param xiNorth
     * @param latitude
     * @return BigDecimal northing
     */
    private static BigDecimal calcNorthing(BigDecimal meridianRadius, BigDecimal 
            xiNorth, BigDecimal latitude) {
        
        BigDecimal northing = (SCALE_FACTOR.multiply(meridianRadius)).multiply(xiNorth);
        
        if(latitude.signum() < 0)
            northing = SOUTH_HEMISPHERE_SUBTRACTION.subtract(northing);
        

        return northing;
        
    }
    
    /**
     * Returns whether the latitude indicates being in the southern or northern
     * hemisphere. 
     * @param latitude
     * @return char Hemisphere
     */
    private static char calcHemisphere(BigDecimal latitude) {
        
        char hemisphere = 'N';
        
        if (latitude.signum() == -1)
            hemisphere = 'S';
        
        return hemisphere;
    }
    
}
