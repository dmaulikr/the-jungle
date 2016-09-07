package br.thejungle.environment.things.info;

import java.io.Serializable;

import br.thejungle.environment.things.Thing;

/**
 * This is an abstract class for all ThingInfos
 */
public abstract class ThingInfo implements Serializable {

	protected float xPos;
	protected float yPos;
	protected float zPos;
	protected boolean isValid;
	private Thing thing;

	public ThingInfo(Thing thing) {
		if(thing==null) throw new NullPointerException("Thing can't be null in ThingInfo constructor");
		isValid = thing.isValid();
		xPos = thing.getXPos();
		yPos = thing.getYPos();
		zPos = thing.getZPos();
		this.thing = thing;
	}

	public float getXPos() {
		return xPos;
	}

	public float getYPos() {
		return yPos;
	}

	public float getZPos() {
		return zPos;
	}
	/**
	 * @return Returns the isValid.
	 */
	public boolean isValid() {
		return isValid;
	}
	
	public String toString() {
		return "ThingInfo: xPos=" + xPos + "; yPos=" + yPos + "; zPos=" + zPos + "; isValid=" + isValid;
	}
	
	private Thing getThing() {
		return thing;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ThingInfo) {
			ThingInfo ti = (ThingInfo)obj;
			return (ti.getThing()==getThing());
		}
		if(obj instanceof Thing) {
			Thing t = (Thing)obj;
			return (t==getThing());
		}
		return false;
	}
}
