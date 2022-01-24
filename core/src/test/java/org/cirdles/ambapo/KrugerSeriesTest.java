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
public class KrugerSeriesTest {
    
    public KrugerSeriesTest() {
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
     * Test of alpha1 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha1() {
        
        System.out.println("alpha1");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.000837732).setScale(9, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha1(flattening3D)).setScale(9, RoundingMode.HALF_UP);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of alpha2 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha2() {
        System.out.println("alpha2");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.000000760852).setScale(12, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha2(flattening3D)).setScale(12, RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }

    /**
     * Test of alpha3 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha3() {
        System.out.println("alpha3");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.00000000119764).setScale(14, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha3(flattening3D)).setScale(14, RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }

    /**
     * Test of alpha4 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha4() {
        System.out.println("alpha4");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.00000000000242917).setScale(17, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha4(flattening3D)).setScale(17, RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }

    /**
     * Test of alpha5 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha5() {
        System.out.println("alpha5");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.00000000000000571181).setScale(
                20, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha5(flattening3D)).setScale(20, 
                RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }

    /**
     * Test of alpha6 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha6() {
        System.out.println("alpha6");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.0000000000000000148).setScale(
                19, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha6(flattening3D)).setScale(19, 
                RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }

    /**
     * Test of alpha7 method, of class KrugerSeries.
     */
    @Test
    public void testAlpha7() {
        System.out.println("alpha7");
        BigDecimal flattening3D = new BigDecimal(0.00167922);
        BigDecimal expResult = new BigDecimal(0.0000000000000000000410768).setScale(
                25, RoundingMode.HALF_UP);
        BigDecimal result = (KrugerSeries.alpha7(flattening3D)).setScale(25, 
                RoundingMode.HALF_UP);
        assertEquals(expResult, result);
    }
    
}
