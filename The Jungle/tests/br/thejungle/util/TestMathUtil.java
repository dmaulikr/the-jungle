/*
 * Created on 11/12/2004
 */
package br.thejungle.util;

import junit.framework.TestCase;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class TestMathUtil extends TestCase {

    public void testIsPointInCircle() {
        boolean is = MathUtil.isPointInCircle(0, 0, 5, 0, 0);
        assertTrue(is);
    }

    public void testNormalizeAngle() {
        double angle = MathUtil.normalizeAngle(360);
        assertEquals(angle, 0, 0);
    }

    public void testPositiveRelative() {
        double angle = MathUtil.positiveRelative(-30);
        assertEquals(angle, 330, 0);
    }

    public void testAngleToDest() {
        double angle = MathUtil.angleToDest(0, 0, 1, 1);
        assertEquals(angle, 45, 0);
    }

    public void testAngleQuarter() {
        int quarter = MathUtil.angleQuarter(120);
        assertEquals(quarter, 2);
    }

    public void testGetDistance() {
        double distance = MathUtil.getDistance(0, 0, 0, 1);
        assertEquals(distance, 1, 0); 
    }
    
	public void testGetProportionalRate() {
        double rate = MathUtil.getProportionalRate(75, 100, 0.50F);
        assertEquals(rate, 0.50, 0); 
	}
}
