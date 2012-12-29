/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hipotetizador;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Victor
 */
public class SensorTest {
    
    public SensorTest() {
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
     * Test of leer method, of class Sensor.
     */
    @Test
    public void testLeer() {
        System.out.println("leer");
        
        Integer num = new Integer(32);
        
        Sensor instance = new Sensor(num,8);
        int[] expResult = {0,0,1,0,0,0,0,0};
        int[] result = instance.leer();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("Passed");
    }

    /**
     * Test of getPrecision method, of class Sensor.
     */

    public void testGetPrecision() {
        System.out.println("getPrecision");
        Sensor instance = null;
        int expResult = 0;
        int result = instance.getPrecision();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPrecision method, of class Sensor.
     */
    
    public void testSetPrecision() {
        System.out.println("setPrecision");
        int precision = 0;
        Sensor instance = null;
        instance.setPrecision(precision);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
