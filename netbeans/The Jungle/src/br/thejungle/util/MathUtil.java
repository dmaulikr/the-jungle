/*
 * MathUtil.java
 *
 * Created on 5 de Dezembro de 2004, 13:36
 */

package br.thejungle.util;

/**
 *
 * @author Flávio
 */
public class MathUtil {
    
    public static boolean isPointInCircle(long pointX, long pointY, long circleX, long circleY, long circleRadius) {
        double r = Math.sqrt((pointX-circleX)^2 + (pointY-circleY)^2);
        return (circleRadius>=r);
    }
    
    public static double getDistance(long x1, long y1, long x2, long y2) {
        return Math.sqrt((x1-x2)^2 + (y1-y2)^2);
    }
    
    public static double getAngle(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        //TODO
        return 45;
    }
}
