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
import org.apache.commons.math3.analysis.function.Atanh;
import com.opencsv.*;
import java.io.*;
import java.util.List;


/**
 *
 * @author Elaina Cole
 */
public class UTMToLatLong {
    
    private static final BigDecimal SCALE_FACTOR = new BigDecimal(0.9996);
    private static final BigDecimal FALSE_EASTING = new BigDecimal(500000);
    private static final BigDecimal ONE = new BigDecimal(1);
    private static final int PRECISION = 20;
    
    /**
     * Converts a csv file of UTM to a csv file of converted Latitudes and Longitudes.
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception 
     */
    public static void bulkConvert(File file) 
        throws FileNotFoundException, IOException, Exception {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile)); CSVReader csvReader = new CSVReader(new FileReader(file))) {
            List<String[]> listOfUTMs = csvReader.readAll();
            
            Datum datum;
            UTM utm;
            Coordinate latAndLong;
            String[] lineToWrite;
            
            for(String[] utmInfo : listOfUTMs) {
                utm = new UTM(
                        new BigDecimal(Double.parseDouble(utmInfo[0].trim().replace("\"", ""))),
                        new BigDecimal(Double.parseDouble(utmInfo[1].trim().replace("\"", ""))),
                        utmInfo[2].trim().replace("\"", "").charAt(0),
                        Integer.parseInt(utmInfo[3].replace("\"", "").trim()),
                        utmInfo[4].trim().replace("\"", "").charAt(0));
                
                datum = Datum.valueOf(utmInfo[5].trim().replace("\"", ""));
                
                latAndLong = UTMToLatLong.convert(utm, datum.getDatum());
                lineToWrite = new String[]{latAndLong.getLatitude().toString(),
                    latAndLong.getLongitude().toString(), datum.getDatum()};
                
                csvWriter.writeNext(lineToWrite);
            }
            
        }
        
    }
    
    /**
     * Converts a UTM into a Coordinate of lat,long with a specific datum.
     * @param utm
     * @param datum
     * @return Coordinate
     * @throws Exception 
     */
    public static Coordinate convert(UTM utm, String datum) throws Exception {
        
        Datum datumInformation = Datum.valueOf(datum);
                
        double[] betaSeries = datumInformation.getBetaSeries();
        
        char hemisphere = utm.getHemisphere();
        
        double zoneCentralMeridian = utm.getZoneNumber() * 6 - 183;
        
        BigDecimal meridianRadius = new BigDecimal(datumInformation.getMeridianRadius());
        
        BigDecimal northing = utm.getNorthing();
        
        BigDecimal easting = utm.getEasting();
        
        BigDecimal xiNorth = calcXiNorth(hemisphere, meridianRadius, northing);
        
        BigDecimal etaEast = calcEtaEast(easting, meridianRadius);
        
        BigDecimal xiPrime = calcXiPrime(xiNorth, etaEast, betaSeries);
        
        BigDecimal etaPrime = calcEtaPrime(xiNorth, etaEast, betaSeries);
        
        BigDecimal tauPrime = calcTauPrime(xiPrime, etaPrime);
        
        BigDecimal eccentricity = new BigDecimal(datumInformation.getEccentricity());
        
        BigDecimal sigma = calcSigma(eccentricity, tauPrime);
        
        BigDecimal longitude = calcLongitude(zoneCentralMeridian, etaPrime, xiPrime);
        
        BigDecimal latitude = calcLatitude(tauPrime, sigma, eccentricity, hemisphere);
        
        Coordinate latAndLong = new Coordinate(latitude, longitude, datum);
        
        return latAndLong;
        
    }
    
    /**
     * Calculates the xi-north which refers to the north-south direction of the UTM.
     * @param hemisphere
     * @param meridianRadius
     * @param northing
     * @return xi north
     */
    private static BigDecimal calcXiNorth(char hemisphere, BigDecimal 
            meridianRadius, BigDecimal northing) {
        
        BigDecimal xiNorth;

        if(hemisphere == 'N') {
            
            BigDecimal divideByThis = SCALE_FACTOR.multiply(meridianRadius).setScale(
                    PRECISION, RoundingMode.HALF_UP);
            
            xiNorth = northing.divide(divideByThis, PRECISION, RoundingMode.HALF_UP);
            
        }
        
        else {
            
            BigDecimal numerator = (new BigDecimal(10000000)).subtract(northing);
            BigDecimal denominator = SCALE_FACTOR.multiply(meridianRadius);
            
            xiNorth = numerator.divide(denominator, PRECISION, RoundingMode.HALF_UP);
            
        }
            
        
        
        return xiNorth;
        
    }
    
    
    /**
     * Eta-east refers to the east west direction of the UTM.
     * @param easting
     * @param meridianRadius
     * @return eta east
     */
    private static BigDecimal calcEtaEast(BigDecimal easting, BigDecimal meridianRadius) {
        
        BigDecimal etaEast = (easting.subtract(FALSE_EASTING)).divide(
            SCALE_FACTOR.multiply(meridianRadius), PRECISION, RoundingMode.HALF_UP);
        
        return etaEast;
    }
    
    /**
     * 
     * @param xiNorth
     * @param etaEast
     * @param betaSeries
     * @return xi prime
     */
    private static BigDecimal calcXiPrime(BigDecimal xiNorth, BigDecimal etaEast, 
            double[] betaSeries) {
        
        double xiNorthDouble = xiNorth.doubleValue();
        double etaEastDouble = etaEast.doubleValue();
        
        BigDecimal sinOfXiNorth;
        BigDecimal coshOfEtaEast;
        
        BigDecimal subtrahend = new BigDecimal(0.0);
        int multiplicand = 2;
        
        for(double beta : betaSeries) {
            
            sinOfXiNorth = new BigDecimal(Math.sin(multiplicand * xiNorthDouble));
            coshOfEtaEast = new BigDecimal(Math.cosh(multiplicand * etaEastDouble));
            
            subtrahend.add(new BigDecimal(beta).multiply(sinOfXiNorth).multiply(coshOfEtaEast));
            
            multiplicand += 2;
            
        }
        
        BigDecimal xiPrime = xiNorth.subtract(subtrahend);
        
        return xiPrime;
        
        
    }
    
    /**
     * 
     * @param xiNorth
     * @param etaEast
     * @param betaSeries
     * @return eta prime
     */
    private static BigDecimal calcEtaPrime(BigDecimal xiNorth, BigDecimal etaEast,
            double[] betaSeries) {
        
        double xiNorthDouble = xiNorth.doubleValue();
        double etaEastDouble = etaEast.doubleValue();
        
        BigDecimal cosOfXiNorth;
        BigDecimal sinhOfEtaEast;
        
        BigDecimal subtrahend = new BigDecimal(0.0);
        int multiplicand = 2;
        
        for(double beta : betaSeries) {
            
            cosOfXiNorth = new BigDecimal(Math.cos(multiplicand * xiNorthDouble));
            sinhOfEtaEast = new BigDecimal(Math.sinh(multiplicand * etaEastDouble));
            
            subtrahend.add(new BigDecimal(beta).multiply(cosOfXiNorth).multiply(sinhOfEtaEast));
            
            multiplicand += 2;
            
        }
        
        BigDecimal etaPrime = etaEast.subtract(subtrahend);
        
        return etaPrime;
        
        
    }
    
    /**
     * 
     * @param xiPrime
     * @param etaPrime
     * @return tau prime
     */
    private static BigDecimal calcTauPrime(BigDecimal xiPrime, BigDecimal etaPrime) {
        
        double xiPrimeDouble = xiPrime.doubleValue();
        double etaPrimeDouble = etaPrime.doubleValue();
        
        BigDecimal sinOfXiPrime = new BigDecimal(Math.sin(xiPrimeDouble));
        BigDecimal cosOfXiPrime = new BigDecimal(Math.cos(xiPrimeDouble));
        BigDecimal sinhOfEtaPrime = new BigDecimal(Math.sinh(etaPrimeDouble));
        
        BigDecimal squareRoot = new BigDecimal(Math.sqrt(sinhOfEtaPrime.pow(2).
                add(cosOfXiPrime.pow(2)).doubleValue()));
        
        BigDecimal tauPrime = sinOfXiPrime.divide(squareRoot, PRECISION, RoundingMode.HALF_UP);
        
        return tauPrime;
    }
    
    /**
     * 
     * @param eccentricity
     * @param tau
     * @return sigma
     */
    private static BigDecimal calcSigma(BigDecimal eccentricity, BigDecimal tau) {
        
        double eccentricityDouble = eccentricity.doubleValue();
        double tauDouble = tau.doubleValue();
        
        Atanh atanh = new Atanh();
        double sigmaDouble = Math.sinh(eccentricityDouble *
            (atanh.value( eccentricityDouble * tauDouble / Math.sqrt(
            1 + Math.pow(tauDouble, 2)))));
        
        BigDecimal sigma = new BigDecimal (sigmaDouble);
        
        return sigma;
        
    }
    
    /**
     * 
     * @param currentTau
     * @param currentSigma
     * @param originalTau
     * @return function of tau
     */
    private static BigDecimal functionOfTau(BigDecimal currentTau, BigDecimal
        currentSigma, BigDecimal originalTau) {
        
        BigDecimal funcOfTau = originalTau.multiply(new BigDecimal(Math.sqrt(1 + 
            currentSigma.pow(2).doubleValue()))).subtract(currentSigma.multiply(
            new BigDecimal(Math.sqrt(1 + currentTau.pow(2).doubleValue())))).subtract(originalTau);
        
        
        return funcOfTau;
        
    }
    

    /**
     * 
     * @param eccentricity
     * @param currentTau
     * @param currentSigma
     * @return change in tau
     */
    private static BigDecimal changeInTau(BigDecimal eccentricity, BigDecimal 
        currentTau, BigDecimal currentSigma) {
        

        BigDecimal changeInTau = ((new BigDecimal(Math.sqrt((1 + 
            currentSigma.pow(2).doubleValue()) * (1 + 
            currentTau.pow(2).doubleValue())))).subtract(currentSigma.multiply(
            currentTau))).multiply(new BigDecimal(1 - eccentricity.pow(
            2).doubleValue())).multiply(new BigDecimal(Math.sqrt(1 + 
            currentTau.pow(2).doubleValue()))).divide(ONE.add(ONE.subtract(
            eccentricity.pow(2))).multiply(currentTau.pow(2)), PRECISION, 
            RoundingMode.HALF_UP);
        
        
        return changeInTau;
        
    }
    
    /**
     * 
     * @param originalTau
     * @param sigma
     * @param eccentricity
     * @param hemisphere
     * @return latitude
     */
    private static BigDecimal calcLatitude(BigDecimal originalTau, BigDecimal sigma, 
            BigDecimal eccentricity, char hemisphere) {
        
        BigDecimal funcOfTau = functionOfTau(originalTau, sigma, originalTau).setScale(PRECISION, RoundingMode.HALF_UP);
        
        BigDecimal changeInTau = changeInTau(eccentricity, originalTau, sigma);
        
        BigDecimal newTau = originalTau.subtract(funcOfTau.divide(changeInTau, 
            PRECISION, RoundingMode.HALF_UP));
          
        BigDecimal latitude = (new BigDecimal(Math.atan(newTau.doubleValue())))
           .multiply(new BigDecimal(180.0 / Math.PI));
        
        if(hemisphere == 'S')
            latitude = latitude.multiply(new BigDecimal(-1));
        
        return latitude;
        
    }
    
    /**
     * 
     * @param zoneCentralMeridian
     * @param etaPrime
     * @param xiPrime
     * @return longitude
     */
    private static BigDecimal calcLongitude(double zoneCentralMeridian, 
        BigDecimal etaPrime, BigDecimal xiPrime) {
        
        double longitudeRadians = Math.atan(Math.sinh(etaPrime.doubleValue())/
            Math.cos(xiPrime.doubleValue()));
        
        BigDecimal changeInLongitude = new BigDecimal(longitudeRadians*180.0/Math.PI);
        
        BigDecimal longitude = new BigDecimal(zoneCentralMeridian).add(changeInLongitude);
        
        return longitude;
    }

    
}
