package br.thejungle.species.abilities;

import java.util.Random;
import java.util.logging.Logger;

import br.thejungle.environment.Task;
import br.thejungle.environment.things.info.ThingInfo;
import br.thejungle.species.Species;

/**
 * Here is implemented the ability of walking
 */
public class Walk extends Ability {

	transient private static Random random = new Random();
	private static Logger logger = Logger.getLogger("br.thejungle.species.abilities.Walk");

	private static final short MAX_SPEED = 30;
	private static final short DEFAULT_SPEED = 4;

	/**
	 * @param species
	 */
	public Walk(Species species) {
		super(species);
	}

	/**
	 * Walk to a desired point in the jungle. The velocity will be the max
	 * allowed. This method is assyncronous.
	 */
	public void gotoPos(float x, float y) {
		gotoPos(x, y, DEFAULT_SPEED);
	}

	public void gotoThing(ThingInfo thing) {
		gotoThing(thing, DEFAULT_SPEED);
	}

	public void gotoThing(ThingInfo thing, short speed) {
	    logger.finest("Adding Walk Task to queue for following thing " + thing.getClass() + " at speed "+speed);
		Task t = new WalkTask(species, thing, getSpeed(speed));
		species.getJungle().executeTask(species.getSpeciesBrain(), t);
	}

	/**
	 * Walk to a desired point in the jungle at certain speed. This method is
	 * assyncronous.
	 */
	public void gotoPos(float x, float y, short speed) {
	    logger.finest("Adding Walk Task to queue for x="+ x +"; y="+y+"; speed="+speed);
		Task t = new WalkTask(species, x, y, getSpeed(speed));
		species.getJungle().executeTask(species.getSpeciesBrain(), t);
	}

	public boolean perform(float directionAngle, float distance, short speed) {
	    logger.finest("Adding Walk Task to queue for directionAngle="+ directionAngle + "; distance=" + distance + "; speed="+speed);
		Task t = new WalkTask(species, directionAngle, distance, getSpeed(speed));
		species.getJungle().executeTask(species.getSpeciesBrain(), t);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.environment.Task#getIndividualID()
	 */
	public String getIndividualID() {
		return species.getIndividualID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#perform()
	 */
	public boolean perform() {
	    return perform(random.nextFloat()*360, (long)(random.nextFloat()*200), DEFAULT_SPEED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#isEffective()
	 */
	public boolean isEffective() {
		return species.getGenetics().getAbilityWalk() >= 0.2;
	}

	private float getSpeed(float desiredSpeed) {
		return Math.min(desiredSpeed, getMaxSpeed());
	}
	
	private short getMaxSpeed() {
		return (short)(species.getAgingMultiplierAscDesc()*species.getGenetics().getAbilityWalk()*MAX_SPEED);	
	}

}
