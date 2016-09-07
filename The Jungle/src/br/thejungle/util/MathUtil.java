/*
 * MathUtil.java
 *
 * Created on 5 de Dezembro de 2004, 13:36
 */

package br.thejungle.util;

import br.thejungle.environment.things.info.ThingInfo;

/**
 * 
 * @author Flávio
 */
public class MathUtil {

	public static boolean isPointInCircle(float circleX, float circleY, float circleRadius, float pointX, float pointY) {
		float r = getDistance(circleX, circleY, pointX, pointY);
		return (circleRadius >= r);
	}

	/**
	 * Retorna um ângulo relativo (entre -180º e 180º) ao ângulo informado
	 * 
	 * @param angle
	 * @return
	 */
	public static float normalizeAngle(float angle) {
		if (angle > -180 && angle <= 180) return angle;
		float fixedAngle = angle;
		while (fixedAngle <= -180)
			fixedAngle += 360;
		while (fixedAngle > 180)
			fixedAngle -= 360;
		return fixedAngle;
	}

	/**
	 * Retorna o primeiro ângulo relativo positivo
	 * 
	 * @param angle
	 * @return
	 */
	public static float positiveRelative(float angle) {
		if (angle >= 0) return angle;
		float fixedAngle = angle;
		while (fixedAngle < 0)
			fixedAngle += 360;
		return fixedAngle;
	}

	public static float angleToDest(float sourceX, float sourceY, float destX, float destY) {
		float a = destX - sourceX;
		float b = destY - sourceY;
		return (b < 0 ? 180 : 0) + (float)Math.toDegrees(Math.atan(a / b)); 
		// a soma em 180 é para os casos em que o ângulo está nos 2º e 3º quadrantes
		// return (b<0?180:0) + Math.toDegrees(Math.atan2(a, b)); 
	}

	public static int angleQuarter(float angle) {
		angle = positiveRelative(angle);
		if (angle >= 0 && angle < 90) {
			return 1;
		} else if (angle >= 90 && angle < 180) {
			return 2;
		} else if (angle >= 180 && angle < 270) {
			return 3;
		} else if (angle >= 270 && angle < 360) {
			return 4;
		} else {
			return angleQuarter(angle - 360);
		}
	}

	public static float getDistance(float sourceX, float sourceY,float destX, float destY) {
		float a = destX - sourceX;
		float b = destY - sourceY;
		float c = (float)Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		return c;
	}
	
	public static float getDistance(ThingInfo thing1, ThingInfo thing2) {
		return getDistance(thing1.getXPos(), thing1.getYPos(), thing2.getXPos(), thing2.getYPos());
	}
	
	public static float cos(double angle) {
	    return (float)Math.cos(Math.toRadians(angle));
	}

	public static float sin(double angle) {
	    return (float)Math.sin(Math.toRadians(angle));
	}
	
	/**
	 * Returns a relation between the interval (maxValue -
	 * percMaxValue*maxValue) and the currentValue This method was created with
	 * help of a pen and a paper, so try to understand it doing so!
	 */
	public static float getProportionalRate(float currentValue, float maxValue,
			float percMaxValue) {
		float da = currentValue - (percMaxValue * maxValue);
		float alt = maxValue - (percMaxValue * maxValue);
		return da / alt;
	}
	
}
