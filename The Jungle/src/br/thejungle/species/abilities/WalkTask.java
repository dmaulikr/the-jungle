/*
 * WalkTask.java
 *
 * Created on 6 de Dezembro de 2004, 23:10
 */

package br.thejungle.species.abilities;

import java.io.Serializable;
import java.util.logging.Logger;

import br.thejungle.environment.TaskSupport;
import br.thejungle.environment.things.info.ThingInfo;
import br.thejungle.species.Species;
import br.thejungle.util.MathUtil;

/**
 * 
 * @author Flávio
 */
public class WalkTask extends TaskSupport implements Serializable {

	private float speed;
	private float xPos, yPos;
	private float directionAngle;
	private float distance;
	private short action;
	private ThingInfo thing;

	private float walked;

	private static Logger logger = Logger.getLogger("br.thejungle.species.abilities.WalkTask");

	private static final short GOTOXY = 1;
	private static final short DIRECTIONAL = 2;
	private static final short FOLLOW_THING = 3;
	
	private static final float FOOD_CONSUME_PER_DISTANCE = 0.01F;
	private static final float FOOD_CONSUME_SPEED_MULTIPLIER = 0.25F;
	
	/** Creates a new instance of WalkTask */
	public WalkTask(Species species, float xPos, float yPos, float speed) {
	    super(species);
		this.action = GOTOXY;
		this.xPos = xPos;
		this.yPos = yPos;
		this.walked = 0;
		this.speed = speed;
	}

	public WalkTask(float directionAngle, float distance, float speed, Species species) {
	    super(species);
		this.action = DIRECTIONAL;
		this.directionAngle = directionAngle;
		this.distance = distance;
		this.species = species;
		this.speed = speed;
	}

	public WalkTask(Species species, ThingInfo thing, float speed) {
	    super(species);
		this.action = FOLLOW_THING;
		this.thing = thing;
		this.species = species;
		this.speed = speed;
	}

    public float getDirectionAngle() {
        return directionAngle;
    }
    public float getSpeed() {
        return speed;
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.environment.Task#stepWork()
	 */
	public void stepWork() {
		float angle = 0;
		float remaining = speed;

		if (action == GOTOXY) {
			remaining = MathUtil.getDistance(species.getXPos(), species.getYPos(), xPos, yPos);
			
			//get the angle to destination
			angle = MathUtil.angleToDest(species.getXPos(), species.getYPos(), xPos, yPos);
			
			// if the target position is within species size, stop
			if (remaining <= 1) {
				//species.setXPos(xPos);
				//species.setYPos(yPos);
				completed = true;
				successful = true;
				return;
			}
			
		} else if (action == FOLLOW_THING) {
			remaining = MathUtil.getDistance(species.getXPos(), species.getYPos(), thing.getXPos(), thing.getYPos());
			
			//verify if the thing is still in species sight range
			if(!species.getSightSense().isSeeing(thing)) {
				completed = true;
				successful = false;
				return;
			}
			
			//get the angle to destination
			angle = MathUtil.angleToDest(species.getXPos(), species.getYPos(), thing.getXPos(), thing.getYPos());

			// if the target position is within species size, stop
			if (remaining <= 1) {
				//species.setXPos(xPos);
				//species.setYPos(yPos);
				completed = true;
				successful = true;
				return;
			}
			
		} else if (action == DIRECTIONAL) {
			angle = directionAngle;
			if (walked >= distance) completed = true;
			
		} else {
			logger.warning("Action in WalkTask isn't GOTOXY, DIRECTION nor FOLLOW_THING. It's value is " + action);
		}
		
		float cspeed = Math.min(Math.max(remaining/5, 1), speed);

		float newX = (species.getXPos() + (cspeed * MathUtil.sin(angle)));
		float newY = (species.getYPos() + (cspeed * MathUtil.cos(angle)));
		
/*		// force to be inside jungle bounds
		newX = Math.max(newX, 0);
		newX = Math.min(newX, species.getJungle().getWidth());
		newY = Math.max(newY, 0);
		newY = Math.min(newY, species.getJungle().getHeight());
*/
		//verify if inside jungle bounds
 		if(newX<=0 || newX>=species.getJungle().getWidth() ||
		   newY<=0 || newY>=species.getJungle().getHeight()) {
			completed = true;
			successful = false;
			return;
		}

 		float step = MathUtil.getDistance(species.getXPos(), species.getYPos(), newX, newY);
		walked += step;
		
		//consume food for walking
		species.consumeFood((float)(step*(FOOD_CONSUME_PER_DISTANCE*(FOOD_CONSUME_SPEED_MULTIPLIER*cspeed))));
		
		species.setXPos(newX);
		species.setYPos(newY);
		
		logger.finest(species.getIndividualID() + ": Step walking to ("+newX + ", " + newY+")");
	}

	/**
	 * @return Returns the xPos.
	 */
	public float getXPos() {
		return xPos;
	}
	/**
	 * @return Returns the yPos.
	 */
	public float getYPos() {
		return yPos;
	}

	public String toString() {
		if(action==DIRECTIONAL) {
			return "WalkTask: "+ super.toString() +"; action=DIRECTIONAL; directionAngle=" + directionAngle + "; distance="+distance + "; speed="+speed + "; walked="+walked;
			
		} else if (action==GOTOXY) {
			return "WalkTask: "+ super.toString() +"; action=GOTOXY; xPos=" + xPos + "; yPos="+yPos + "; speed="+speed + "; walked="+walked;
			
		} else if(action==FOLLOW_THING) {
			return "WalkTask: "+ super.toString() +"; action=FOLLOW_THING; speed="+speed + "; walked="+walked + "; thingInfo=" + thing;
			
		} else {
		    return super.toString();
		}
	}
}
