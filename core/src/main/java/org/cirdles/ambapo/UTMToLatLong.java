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
import java.math.MathContext;
import java.math.RoundingMode;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 *
 * @author Elaina Cole
 */
public class UTMToLatLong {
    
    private static final BigDecimal SCALE_FACTOR = new BigDecimal(0.9996);
    private static final BigDecimal FALSE_EASTING = new BigDecimal(500000);
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal THREE = new BigDecimal(3);
    private static final BigDecimal FOUR = new BigDecimal(4);
    private static final BigDecimal FIVE = new BigDecimal(5);
    private static final BigDecimal THIRTYTWO = new BigDecimal(32);
    private static final BigDecimal ONEEIGHTY = new BigDecimal(180);
    
    private static final int SCALE = 34;
    private static final MathContext MC = new MathContext(34, RoundingMode.HALF_UP);

    /**
     * Converts a UTM into a Coordinate of lat,long with a specific datum.
     * @param utm
     * @param datum
     * @return Coordinate
     * @throws Exception 
     */
    public static Coordinate convert(UTM utm, String datum) throws Exception {
        
        Datum datumInformation = Datum.valueOf(datum);
        char hemisphere = utm.getHemisphere();
        BigDecimal northing = utm.getNorthing();
        BigDecimal easting = utm.getEasting();
        
        BigDecimal eccentricity = new BigDecimal(datumInformation.getEccentricity());
        BigDecimal equatorialRadius = new BigDecimal(datumInformation.getEquatorialRadius());
        BigDecimal polarAxis = new BigDecimal(datumInformation.getPolarRadius());
        BigDecimal firstEccentricitySquared = getFirstEccentricitySquared(polarAxis, equatorialRadius);
        BigDecimal mu = getMu(northing, eccentricity, equatorialRadius);
        BigDecimal ei = getFirstEccentricity(eccentricity);
        BigDecimal footprintLatitude = getFootprintLatitude(ei, mu);
        BigDecimal oneMinusEccentricityTimesSinOfFootprintLatSquared = getOneMinusEccentricityTimesSinOfFootprintLatSquared(footprintLatitude, eccentricity);
        BigDecimal coefficientN1 = getCoefficientN1(equatorialRadius, oneMinusEccentricityTimesSinOfFootprintLatSquared);
        BigDecimal coefficientD = getCoefficientD(easting, coefficientN1);
        BigDecimal coefficientT1 = getCoefficientT1(footprintLatitude);
        BigDecimal coefficientC1 = getCoefficientC1(ei, footprintLatitude);
        
        /* calcLongitude(int zoneNumber, BigDecimal coefficientD,
            BigDecimal coefficientC1, BigDecimal coefficientT1, BigDecimal footprintLat,
            BigDecimal firstEccentricitySquared) */
        BigDecimal longitude = calcLongitude(utm.getZoneNumber(), coefficientD, coefficientC1,
                coefficientT1, footprintLatitude, firstEccentricitySquared);
        
        if(longitude.doubleValue() > Coordinate.MAX_LONGITUDE)
            longitude = new BigDecimal(Coordinate.MAX_LONGITUDE);
        
        if(longitude.doubleValue() < Coordinate.MIN_LONGITUDE)
            longitude = new BigDecimal(Coordinate.MIN_LONGITUDE);
      
        /* calcLatitude(BigDecimal oneMinusEccentricityTimesSinOfFootprintLatSquared,
        BigDecimal equatorialRadius, BigDecimal eccentricity, BigDecimal e1Squared,
        BigDecimal footprintLat, BigDecimal coefficientC1, BigDecimal coefficientN1,
        BigDecimal coefficientD, BigDecimal coefficientT1, char hemisphere)
        */
        BigDecimal latitude = calcLatitude(oneMinusEccentricityTimesSinOfFootprintLatSquared,
                equatorialRadius, eccentricity, firstEccentricitySquared, footprintLatitude,
                coefficientC1, coefficientN1, coefficientD, coefficientT1, hemisphere);
        
        if(latitude.doubleValue() > Coordinate.MAX_LATITUDE)
            latitude = new BigDecimal(Coordinate.MAX_LATITUDE);
        if(latitude.doubleValue() < Coordinate.MIN_LATITUDE)
            latitude = new BigDecimal(Coordinate.MIN_LATITUDE);
        
        Coordinate latAndLong = new Coordinate(latitude.setScale(SCALE, RoundingMode.HALF_UP), longitude.setScale(SCALE, RoundingMode.HALF_UP), datum);
        
        return latAndLong;
        
    }
    
    private static BigDecimal getOneMinusEccentricityTimesSinOfFootprintLatSquared(BigDecimal footprintLat, BigDecimal eccentricity)
    {
        BigDecimal sinOfFootprintLat = BigDecimalMath.sin(footprintLat, MC);
        BigDecimal eccentricityTimesSinOfFootprintLat = eccentricity.multiply(sinOfFootprintLat);
        BigDecimal eccentricityTimesSinOfFootprintLatSquared = eccentricityTimesSinOfFootprintLat.pow(2);
        return BigDecimal.ONE.subtract(eccentricityTimesSinOfFootprintLatSquared);
    }
    
    private static BigDecimal getCoefficientC1(BigDecimal firstEccentricity, BigDecimal footprintLat)
    {
        BigDecimal eiSquared = firstEccentricity.pow(2);
        BigDecimal cosOfFootprintLatSquared = BigDecimalMath.cos(footprintLat, MC).pow(2);
        return eiSquared.multiply(cosOfFootprintLatSquared); //M3
    }
    
    private static BigDecimal getCoefficientT1(BigDecimal footprintLatitude)
    {
        BigDecimal tanOfFootprintLat = BigDecimalMath.tan(footprintLatitude, MC);
        return tanOfFootprintLat.pow(2); //N3
    }
    
    private static BigDecimal getCoefficientN1(BigDecimal equatorialRadius, BigDecimal oneMinusEccentricityTimesSinOfFootprintLatSquared)
    {
        BigDecimal sqrtOfOneMinusEccentricityTimesSinOfFootprintLatSquared = oneMinusEccentricityTimesSinOfFootprintLatSquared.sqrt(MC);
        return equatorialRadius.divide(sqrtOfOneMinusEccentricityTimesSinOfFootprintLatSquared, MC); //O3
    }
    
    private static BigDecimal getCoefficientD(BigDecimal easting, BigDecimal coefficientN1)
    {
        BigDecimal eastPrime = FALSE_EASTING.subtract(easting); //I3
        BigDecimal n1TimesScale = coefficientN1.multiply(SCALE_FACTOR);
        return eastPrime.divide(n1TimesScale, MC); //Q3
    }
    
    private static BigDecimal getMu(BigDecimal northing, BigDecimal eccentricity, BigDecimal equatorialRadius)
    {
        BigDecimal arcLength = northing.divide(SCALE_FACTOR, MC); //J3
        BigDecimal eccentricitySq = eccentricity.pow(2);
        BigDecimal eccentricityFourth = eccentricity.pow(4);
        BigDecimal eccentricitySixth = eccentricity.pow(6);
        BigDecimal eccentricityFourthTimesThree = eccentricityFourth.multiply(THREE);
        BigDecimal eccentricitySixthTimesFive = eccentricitySixth.multiply(FIVE);
        BigDecimal eccentricitySqOverFour = eccentricitySq.divide(FOUR, MC);
        BigDecimal eccentricityFourthTimesThreeOverSixtyFour = eccentricityFourthTimesThree.divide(new BigDecimal(64), MC);
        BigDecimal eccentricitySixthTimesFiveOver256 = eccentricitySixthTimesFive.divide(new BigDecimal(256), MC);
        BigDecimal oneMinusEccentricities = BigDecimal.ONE.subtract(eccentricitySqOverFour).subtract(eccentricityFourthTimesThreeOverSixtyFour).subtract(eccentricitySixthTimesFiveOver256);
        BigDecimal eqRadiusTimesEccentricities = equatorialRadius.multiply(oneMinusEccentricities);
        return arcLength.divide(eqRadiusTimesEccentricities, MC); // K3
    }
    
    private static BigDecimal getFirstEccentricity(BigDecimal eccentricity)
    {
        BigDecimal eccentricitySq = eccentricity.pow(2);
        BigDecimal oneMinusEccentrictySquared = BigDecimal.ONE.subtract(eccentricitySq);
        BigDecimal sqRootOfOneMinusEccentrictySquared = oneMinusEccentrictySquared.sqrt(MC);
        BigDecimal oneMinusSqRootOfOneMinusEccentrictySquared = BigDecimal.ONE.subtract(sqRootOfOneMinusEccentrictySquared);
        BigDecimal onePlusSqRootOfOneMinusEccentrictySquared = BigDecimal.ONE.add(sqRootOfOneMinusEccentrictySquared);
        return oneMinusSqRootOfOneMinusEccentrictySquared.divide(onePlusSqRootOfOneMinusEccentrictySquared, MC);
    }
    
    private static BigDecimal getFirstEccentricitySquared(BigDecimal polarRadius, BigDecimal equatorialRadius)
    {
        BigDecimal polarRadiusSquared = polarRadius.pow(2);
        BigDecimal equatorialRadiusSquared = equatorialRadius.pow(2);
        BigDecimal polarRadiusSquaredOverEquatorialRadiusSquared = polarRadiusSquared.divide(equatorialRadiusSquared, MC);
        return BigDecimal.ONE.subtract(polarRadiusSquaredOverEquatorialRadiusSquared);
    }
    
    private static BigDecimal getFootprintLatitude(BigDecimal ei, BigDecimal mu)
    {
        BigDecimal eiCubed = ei.pow(3);
        BigDecimal threeTimesEi = ei.multiply(THREE);
        BigDecimal twentySevenTimesEiCubed = eiCubed.multiply(new BigDecimal(27));
        BigDecimal threeTimesEiOverTwo = threeTimesEi.divide(TWO, MC);
        BigDecimal twentySevenTimesEiCubedOverThirtyTwo = twentySevenTimesEiCubed.divide(THIRTYTWO, MC);
        BigDecimal c1 = threeTimesEiOverTwo.subtract(twentySevenTimesEiCubedOverThirtyTwo); // B14

        BigDecimal eiSquared = ei.pow(2);
        BigDecimal eiToTheFourth = ei.pow(4);
        BigDecimal twentyOneTimesEiSquared = new BigDecimal(21).multiply(eiSquared);
        BigDecimal twentyOneTimesEiSquaredOverSixteen = twentyOneTimesEiSquared.divide(new BigDecimal(16), MC);
        BigDecimal fiftyFiveTimesEiToTheFourth = new BigDecimal(55).multiply(eiToTheFourth);
        BigDecimal fiftyFiveTimesEiToTheFourthOverThirtyTwo = fiftyFiveTimesEiToTheFourth.divide(THIRTYTWO, MC);
        BigDecimal c2 = twentyOneTimesEiSquaredOverSixteen.subtract(fiftyFiveTimesEiToTheFourthOverThirtyTwo); // B15
        
        BigDecimal oneFiftyOneTimesEiCubed = new BigDecimal(151).multiply(eiCubed);
        BigDecimal c3 = oneFiftyOneTimesEiCubed.divide(new BigDecimal(96), MC); //B16
        
        BigDecimal oneThousandNinetySevenTimesEiToTheFourth = new BigDecimal(1097).multiply(eiToTheFourth);
        BigDecimal c4 = oneThousandNinetySevenTimesEiToTheFourth.divide(new BigDecimal(512), MC);
        
        BigDecimal sinOfTwoTimesMu = BigDecimalMath.sin(TWO.multiply(mu), MC);
        BigDecimal sinOfFourTimesMu = BigDecimalMath.sin(FOUR.multiply(mu), MC);
        BigDecimal sinOfSixTimesMu = BigDecimalMath.sin(new BigDecimal(6).multiply(mu), MC);
        BigDecimal sinOfEightTimesMu = BigDecimalMath.sin(new BigDecimal(8).multiply(mu), MC);
        BigDecimal c1TimesSinOfTwoTimesMu = c1.multiply(sinOfTwoTimesMu);
        BigDecimal c2TimesSinOfFourTimesMu = c2.multiply(sinOfFourTimesMu);
        BigDecimal c3TimesSinOfSixTimesMu = c3.multiply(sinOfSixTimesMu);
        BigDecimal c4TimesSinOfEightTimesMu = c4.multiply(sinOfEightTimesMu);
        return mu.add(c1TimesSinOfTwoTimesMu).add(c2TimesSinOfFourTimesMu).add(c3TimesSinOfSixTimesMu).add(c4TimesSinOfEightTimesMu);
    }
    
    /**
     * 
     * @param originalTau
     * @param sigma
     * @param eccentricity
     * @param hemisphere
     * @return latitude
     */
    private static BigDecimal calcLatitude(BigDecimal oneMinusEccentricityTimesSinOfFootprintLatSquared,
        BigDecimal equatorialRadius, BigDecimal eccentricity, BigDecimal e1Squared,
        BigDecimal footprintLat, BigDecimal coefficientC1, BigDecimal coefficientN1,
        BigDecimal coefficientD, BigDecimal coefficientT1, char hemisphere)
    {
        BigDecimal eccentricitySq = eccentricity.pow(2);
        BigDecimal oneMinusEccentrictySquared = BigDecimal.ONE.subtract(eccentricitySq);
        BigDecimal oneMinusEccentricityTimesSinOfFootprintLatSquaredCubed = oneMinusEccentricityTimesSinOfFootprintLatSquared.pow(3);
        BigDecimal sqrtOneMinusEccentricityTimesSinOfFootprintLatSquaredCubed = oneMinusEccentricityTimesSinOfFootprintLatSquaredCubed.sqrt(MC);
        BigDecimal equatorialRadiusTimesOneMinusEccentrictySquared = equatorialRadius.multiply(oneMinusEccentrictySquared);
        //P3
        BigDecimal coefficient_r1 = equatorialRadiusTimesOneMinusEccentrictySquared.divide(sqrtOneMinusEccentricityTimesSinOfFootprintLatSquaredCubed, MC);
        
        BigDecimal tanOfFootprintLat = BigDecimalMath.tan(footprintLat, MC);
        BigDecimal n1TimesTanOfFootprintLat = coefficientN1.multiply(tanOfFootprintLat);
        BigDecimal fact1 = n1TimesTanOfFootprintLat.divide(coefficient_r1, MC); //R3
        
        BigDecimal fact2 = coefficientD.pow(2).divide(TWO, MC); //S3
        
        BigDecimal threeTimesCoefficientT1 = THREE.multiply(coefficientT1);
        BigDecimal tenTimesCoefficientC1 = BigDecimal.TEN.multiply(coefficientC1);
        BigDecimal fourTimesCoefficientC1Squared = FOUR.multiply(coefficientC1).multiply(coefficientC1);
        BigDecimal nineTimesE1Squared = new BigDecimal(9).multiply(e1Squared);
        BigDecimal sumOfCoefficientsForFact3 = FIVE.add(threeTimesCoefficientT1).add(tenTimesCoefficientC1).subtract(fourTimesCoefficientC1Squared).subtract(nineTimesE1Squared);
        BigDecimal coefficientDToTheFourth = coefficientD.pow(4);
        BigDecimal fact3 = sumOfCoefficientsForFact3.multiply(coefficientDToTheFourth).divide(new BigDecimal(24), MC); //T3

        BigDecimal ninetyTimesCoefficientT1 = new BigDecimal(90).multiply(coefficientT1);
        BigDecimal twoNinetyEightTimesCoefficientC1 = new BigDecimal(298).multiply(coefficientC1);
        BigDecimal fortyFiveTimesCoefficientT1Squared = new BigDecimal(45).multiply(coefficientT1).multiply(coefficientT1);
        BigDecimal twoFiftyTwoTimesE1Squared = new BigDecimal(252).multiply(e1Squared);
        BigDecimal threeTimesCoefficientC1Squared = THREE.multiply(coefficientC1).multiply(coefficientC1);
        BigDecimal sumOfCoefficientsForFact4 = new BigDecimal(61).add(ninetyTimesCoefficientT1).add(twoNinetyEightTimesCoefficientC1).add(fortyFiveTimesCoefficientT1Squared).subtract(twoFiftyTwoTimesE1Squared).subtract(threeTimesCoefficientC1Squared);
        BigDecimal coefficientDToTheSixth = coefficientD.pow(6);
        BigDecimal fact4 = sumOfCoefficientsForFact4.multiply(coefficientDToTheSixth).divide(new BigDecimal(720), MC); //U3
        
        BigDecimal sumOfFacts = fact2.add(fact3).add(fact4);
        BigDecimal fact1TimesSumOfFacts = fact1.multiply(sumOfFacts);
        BigDecimal footprintLatMinusFact1TimesSumOfFacts = footprintLat.subtract(fact1TimesSumOfFacts);
        BigDecimal oneEightyTimesFootprintLatMinusFact1TimesSumOfFacts = ONEEIGHTY.multiply(footprintLatMinusFact1TimesSumOfFacts);
        BigDecimal latitude = oneEightyTimesFootprintLatMinusFact1TimesSumOfFacts.divide(BigDecimalMath.pi(MC), MC);
        
        return (hemisphere == 'N' ? latitude : new BigDecimal(-1).multiply(latitude));
    }
    
    /**
     * 
     * @param zoneCentralMeridian
     * @param etaPrime
     * @param xiPrime
     * @return longitude
     */
    private static BigDecimal calcLongitude(int zoneNumber, BigDecimal coefficientD,
            BigDecimal coefficientC1, BigDecimal coefficientT1, BigDecimal footprintLat,
            BigDecimal firstEccentricitySquared) 
    {
        BigDecimal six = new BigDecimal(6);
        BigDecimal eight = new BigDecimal(8);
        BigDecimal twentyFour = new BigDecimal(24);
        BigDecimal twentyEight = new BigDecimal(28);
        BigDecimal oneTwenty = new BigDecimal(120);
        
        BigDecimal zoneCentralMeridian = new BigDecimal(zoneNumber * 6 - 183); //Z3
        
        BigDecimal twoTimesCoefficientT1 = coefficientT1.multiply(TWO);
        BigDecimal sumOfCoefficientsForFact2 = BigDecimal.ONE.add(twoTimesCoefficientT1).add(coefficientC1);
        BigDecimal coefficientDCubed = coefficientD.pow(3);
        BigDecimal sumOfCoefficientsForFact2TimesCoefficientDCubed = sumOfCoefficientsForFact2.multiply(coefficientDCubed);
        BigDecimal fact2 = sumOfCoefficientsForFact2TimesCoefficientDCubed.divide(six, MC);
        
        BigDecimal coefficientC1Squared = coefficientC1.pow(2);
        BigDecimal coefficientT1Squared = coefficientT1.pow(2);
        BigDecimal twoTimesCoefficientC1 = coefficientC1.multiply(TWO);
        BigDecimal twentyEightTimesCoefficientT1 = coefficientT1.multiply(twentyEight);
        BigDecimal threeTimesCoefficientC1Squared = coefficientC1Squared.multiply(THREE);
        BigDecimal eightTimesFirstEccentricitySquared = firstEccentricitySquared.multiply(eight);
        BigDecimal twentyFourTimesCoefficientT1Squared = coefficientT1Squared.multiply(twentyFour);
        BigDecimal sumOfCoefficientsForFact3 = FIVE.subtract(twoTimesCoefficientC1).add(twentyEightTimesCoefficientT1).subtract(threeTimesCoefficientC1Squared).add(eightTimesFirstEccentricitySquared).add(twentyFourTimesCoefficientT1Squared);
        BigDecimal coefficientDToTheFifth = coefficientD.pow(5);
        BigDecimal fact3 = sumOfCoefficientsForFact3.multiply(coefficientDToTheFifth).divide(oneTwenty, MC);
        
        BigDecimal cosOfFootprintLat = BigDecimalMath.cos(footprintLat, MC);
        BigDecimal sumOfFacts = coefficientD.subtract(fact2).add(fact3);
        BigDecimal deltaLong = sumOfFacts.divide(cosOfFootprintLat, MC);
        
        BigDecimal deltaLongTimesOneEighty = deltaLong.multiply(ONEEIGHTY);
        BigDecimal deltaLongTimesOneEightyOverPi = deltaLongTimesOneEighty.divide(BigDecimalMath.pi(MC), MC);
        return zoneCentralMeridian.subtract(deltaLongTimesOneEightyOverPi);
    }

    
}
