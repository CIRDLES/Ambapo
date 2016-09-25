/*
 * KrugerSeries.java
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
import java.math.MathContext;

/**
 * Calculates Alpha and Beta Series numbers for a datum using its flattening3D.
 * @author Elaina Cole
 */
public class KrugerSeries {
    
    /**
     * Returns first number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha1(BigDecimal flattening3D) {
        
        BigDecimal alpha1= new BigDecimal(1.0/2.0).multiply(flattening3D).subtract(
            new BigDecimal(2.0/3.0).multiply(flattening3D.pow(2))).add(
            new BigDecimal(5.0/16.0).multiply(flattening3D.pow(3))).add(
            new BigDecimal(41.0/180.0).multiply(flattening3D.pow(4))).subtract(
            new BigDecimal(127.0/288.0).multiply(flattening3D.pow(5))).add(
            new BigDecimal(7891.0/37800.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(72161.0/387072.0).multiply(flattening3D.pow(7))).subtract(
            new BigDecimal(18975107.0/50803200.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(60193001.0/290304000.0).multiply(flattening3D.pow(9))).add(
            new BigDecimal(134592031/1026432000).multiply(flattening3D.pow(10)));
        
        return alpha1;
    }
    
    /**
     * Returns second number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha2(BigDecimal flattening3D) {

        BigDecimal alpha2= new BigDecimal(13.0/48.0).multiply(flattening3D.pow(2)).subtract(
            new BigDecimal(3.0/5.0).multiply(flattening3D.pow(3))).add(
            new BigDecimal(557.0/1440.0).multiply(flattening3D.pow(4))).add(
            new BigDecimal(281.0/630.0).multiply(flattening3D.pow(5))).subtract(
            new BigDecimal(1983433.0/1935360.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(13769.0/28800.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(148003883.0/174182400.0).multiply(flattening3D.pow(8))).subtract(
            new BigDecimal(705286231.0/465696000.0).multiply(flattening3D.pow(9))).add(
            new BigDecimal(1703267974087.0/3218890752000.0).multiply(flattening3D.pow(10)));
        
        return alpha2;
    }
    
    /**
     * Returns third number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha3(BigDecimal flattening3D) {
        
        BigDecimal alpha3= new BigDecimal(61.0/240.0).multiply(flattening3D.pow(3)).subtract(
            new BigDecimal(103.0/140.0).multiply(flattening3D.pow(4))).add(
            new BigDecimal(15061.0/26880.0).multiply(flattening3D.pow(5))).add(
            new BigDecimal(167603.0/181440.0).multiply(flattening3D.pow(6))).subtract(
            new BigDecimal(67102379/29030400).multiply(flattening3D.pow(7))).add(
            new BigDecimal(79682431.0/79833600.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(6304945039.0/2128896000.0).multiply(flattening3D.pow(9))).subtract(
            new BigDecimal(6601904925257.0/1307674368000.0).multiply(flattening3D.pow(10)));
    
        return alpha3;
    }
    
    /**
     * Returns fourth number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha4(BigDecimal flattening3D) {
        
        BigDecimal alpha4= new BigDecimal(49561.0/161280.0).multiply(flattening3D.pow(4)).subtract(
            new BigDecimal(179.0/168.0).multiply(flattening3D.pow(5))).add(
            new BigDecimal(6601661.0/7257600.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(97445.0/49896.0).multiply(flattening3D.pow(7))).subtract(
            new BigDecimal(40176129013.0/7664025600.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(138471097.0/66528000.0).multiply(flattening3D.pow(9))).add(
            new BigDecimal(48087451385201.0/5230697472000.0).multiply(flattening3D.pow(10)));
        
        return alpha4;
    }
    
    /**
     * Returns fifth number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha5(BigDecimal flattening3D) {
        
        BigDecimal alpha5= new BigDecimal(34729.0/80640.0).multiply(flattening3D.pow(5)).subtract(
            new BigDecimal(3418889.0/1995840.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(14644087.0/9123840.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(2605413599.0/622702080.0).multiply(flattening3D.pow(8))).subtract(
            new BigDecimal(31015475399.0/2583060480.0).multiply(flattening3D.pow(9))).add(
            new BigDecimal(5820486440369.0/1307674368000.0).multiply(flattening3D.pow(10)));
    
        return alpha5;
    }
    
    /**
     * Returns sixth number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha6(BigDecimal flattening3D) {
        
        BigDecimal alpha6= new BigDecimal(212378941.0/319334400.0).multiply(flattening3D.pow(6)).subtract(
            new BigDecimal(30705481.0/10378368.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(175214326799.0/58118860800.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(870492877.0/96096000.0).multiply(flattening3D.pow(9))).subtract(
            new BigDecimal(1328004581729000.0/47823519744000.0).multiply(flattening3D.pow(10)));
    
        return alpha6;
    }
    
    /**
     * Returns seventh number in alpha series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal alpha7(BigDecimal flattening3D) {
        BigDecimal alpha7= new BigDecimal(1522256789.0/1383782400.0).multiply(flattening3D.pow(
                7)).subtract(new BigDecimal(16759934899.0/3113510400.0).multiply(
                flattening3D.pow(8))).add(new BigDecimal(1315149374443.0/
                221405184000.0).multiply(flattening3D.pow(9))).add(
                new BigDecimal(71809987837451.0/3629463552000.0).multiply(flattening3D.pow(10)));
    
        return alpha7;
    }
    
    /**
     * Returns first number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta1(BigDecimal flattening3D) {
        
        BigDecimal beta1 = (new BigDecimal(.5).multiply(flattening3D)).subtract(
            new BigDecimal(2.0/3.0).multiply(flattening3D.pow(2))).add(
            new BigDecimal(37.0/96.0).multiply(flattening3D.pow(3))).subtract(
            new BigDecimal(1.0/360.0).multiply(flattening3D.pow(4))).subtract(
            new BigDecimal(81.0/512.0).multiply(flattening3D.pow(5))).add(
            new BigDecimal(96199.0/604800.0).multiply(flattening3D.pow(6))).subtract(
            new BigDecimal(5406467.0/38707200.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(7944359.0/67737600.0).multiply(flattening3D.pow(8))).subtract(
            new BigDecimal(7378753979.0/97542144000.0).multiply(flattening3D.pow(9))).add(
            new BigDecimal(25123531261.0/804722688000.0).multiply(flattening3D.pow(10)));
        
        
        return beta1;
              
    }
    
    /**
     * Returns second number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta2(BigDecimal flattening3D) {
        
        BigDecimal beta2 = (new BigDecimal(1.0/48.0, MathContext.DECIMAL128)).multiply(flattening3D.pow(2)).add(
            new BigDecimal(1.0/15.0).multiply(flattening3D.pow(3))).subtract(
            new BigDecimal(437.0-1440.0).multiply(flattening3D.pow(4))).add(
            new BigDecimal(46.0/105.0).multiply(flattening3D.pow(5))).subtract(
            new BigDecimal(1118711.0/3870720.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(51841.0/1209600.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(24749483.0/348364800.0).multiply(flattening3D.pow(8))).subtract(
            new BigDecimal(115295683.0/1397088000.0)).multiply(flattening3D.pow(9)).add(
            new BigDecimal(5487737251099.0/51502252032000.0).multiply(flattening3D.pow(10)));
        
        
        return beta2;
        
    }
    
    
    /**
     * Returns third number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta3(BigDecimal flattening3D) {
        
        BigDecimal beta3 = (new BigDecimal(17.0/480.0).multiply(flattening3D.pow(3))).subtract(
            new BigDecimal(37.0/840.0).multiply(flattening3D.pow(4))).subtract(
            new BigDecimal(209.0/4480.0).multiply(flattening3D.pow(5))).add(
            new BigDecimal(5569.0/90720.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(9261899.0/58060800.0).multiply(flattening3D.pow(7))).subtract(
            new BigDecimal(6457463.0/17740800.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(2473691167.0/9289728000.0)).multiply(flattening3D.pow(9)).subtract(
            new BigDecimal(5487737251099.0/51502252032000.0).multiply(flattening3D.pow(10)));
        
        
        return beta3;
        
    }
    
    /**
     * Returns fourth number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta4(BigDecimal flattening3D) {
        
        BigDecimal beta4 = (new BigDecimal(4397.0/16128.0).multiply(
            flattening3D.pow(4))).subtract(
            new BigDecimal(11.0/504.0).multiply(flattening3D.pow(5))).subtract(
            new BigDecimal(830251.0/7257600.0).multiply(flattening3D.pow(6))).add(
            new BigDecimal(466511.0/2494800.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(324154477.0/7664025600.0).multiply(
            flattening3D.pow(8))).subtract(new BigDecimal(937932223.0/3891888000.0))
            .multiply(flattening3D.pow(9)).subtract(new BigDecimal(
            89112264211.0/5230697472000.0).multiply(flattening3D.pow(10)));
        
        return beta4;
        
    }
    
    /**
     * Returns fifth number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta5(BigDecimal flattening3D) {
        
        BigDecimal beta5 = (new BigDecimal(4583.0/161280.0).multiply(
            flattening3D.pow(5))).subtract(
            new BigDecimal(108847.0/3991680.0).multiply(flattening3D.pow(6))).subtract(
            new BigDecimal(8005831.0/63866880.0).multiply(flattening3D.pow(7))).add(
            new BigDecimal(22894433.0/124540416.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(112731569449.0/557941063680.0)).multiply(
            flattening3D.pow(9)).subtract(new BigDecimal(
            5391039814733.0/10461394944000.0).multiply(flattening3D.pow(10)));
        
        return beta5;
        
    }
    
    /**
     * Returns sixth number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta6(BigDecimal flattening3D) {
        
        BigDecimal beta6 = (new BigDecimal(20648693.0/6386688000.0).multiply(
            flattening3D.pow(6))).subtract(
            new BigDecimal(16363163.0/518918400.0).multiply(flattening3D.pow(7))).subtract(
            new BigDecimal(2204645983.0/12915302400.0).multiply(flattening3D.pow(8))).add(
            new BigDecimal(4543317553.0/18162144000.0)).multiply(flattening3D.pow(9)).add(
            new BigDecimal(54894890298749.0/167382319104000.0).multiply(flattening3D.pow(10)));
        
        return beta6;
        
    }
    
    /**
     * Returns seventh number in beta series.
     * @param flattening3D
     * @return 
     */
    public static BigDecimal beta7(BigDecimal flattening3D) {
        
        BigDecimal beta7 = (new BigDecimal(219941297.0/5535129600.0).multiply(
            flattening3D.pow(7))).subtract(
            new BigDecimal(497323811.0/12454041600.0).multiply(
            flattening3D.pow(8))).subtract(
            new BigDecimal(79431132943.0/332107776000.0)).multiply(
            flattening3D.pow(9)).add(
            new BigDecimal(4346429528407.0/12703122432000.0).multiply(flattening3D.pow(10)));
        
        return beta7;
        
    }
}
