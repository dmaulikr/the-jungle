package br.thejungle.environment.things;

import java.io.Serializable;

import br.thejungle.environment.things.info.ThingInfo;

/**
 * This is an abstract class for all things in the jungle
 */
public abstract class Thing implements Serializable {

	private float xPos, yPos, zPos;

	/**
	 * @return Returns the xPos.
	 */
	public float getXPos() {
		return xPos;
	}

	/**
	 * @param pos
	 *            The xPos to set.
	 */
	public void setXPos(float pos) {
		xPos = pos;
	}

	/**
	 * @return Returns the yPos.
	 */
	public float getYPos() {
		return yPos;
	}

	/**
	 * @param pos
	 *            The yPos to set.
	 */
	public void setYPos(float pos) {
		yPos = pos;
	}

	/**
	 * @return Returns the zPos.
	 */
	public float getZPos() {
		return zPos;
	}

	/**
	 * @param pos
	 *            The zPos to set.
	 */
	public void setZPos(float pos) {
		zPos = pos;
	}

	public abstract void timeElapsed();

	public abstract boolean isValid();
	
	public abstract ThingInfo getThingInfo();

	public String toString() {
		return "xPos=" + xPos + "; yPos=" + yPos + "; zPos=" + zPos;
	}

}
