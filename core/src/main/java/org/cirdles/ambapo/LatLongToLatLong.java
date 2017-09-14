/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import java.math.BigDecimal;

/**
 *
 * @author evc1996
 */
public class LatLongToLatLong {
    
    public static Coordinate convert(BigDecimal latitude, BigDecimal longitude, String fromDatum, String toDatum) throws Exception
    {
        UTM utm = LatLongToUTM.convert(latitude, longitude, fromDatum);
        Coordinate coordinate = UTMToLatLong.convert(utm, toDatum);
        
        return coordinate;
    }
}
