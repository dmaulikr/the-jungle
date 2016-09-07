package br.thejungle.species.senses;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import br.thejungle.environment.things.Thing;
import br.thejungle.environment.things.info.ThingInfo;
import br.thejungle.species.Species;

/**
 * This is the Sight sense. Here is implemented how the Species can see
 * anything.
 */
public class Sight extends Sense {

	private static Logger logger = Logger.getLogger("br.thejungle.species.senses.Sight");
	private CopyOnWriteArrayList<SightEvent> sightEvents = new CopyOnWriteArrayList<SightEvent>();
	
	public static final int MAX_SIGHT_RANGE = 200;
	public static final short MAX_EVENTS_HISTORY = 10;
	
	public Sight(Species species) {
		super(species);
	}

	public void work() {
		
		logger.finest(species.getIndividualID() + " is trying to see anything");
		CopyOnWriteArrayList<Thing> foundThings = getThingsAtSight();
		
		for(Thing thing: foundThings) {
			if(thing.equals(species)) 
				continue; //i am seeing myself, hehe!
			
			SightEvent event = new SightEvent(thing);
			
			//verify if this same event wasn't fired recently 
			if(sightEvents.contains(event)) 
				continue; //this same thing was already seen at the same place!
			
			logger.finest(species.getIndividualID() + " saw " + thing);
			
			//store event in history
			sightEvents.add(0, event);
			if(sightEvents.size()>MAX_EVENTS_HISTORY) {
				sightEvents.remove(MAX_EVENTS_HISTORY);
			}
			
			species.getSpeciesBrain().senseFired(event);
		}
	}

	public CopyOnWriteArrayList<Thing> getThingsAtSight() {
		return species.getJungle().getThings(species.getXPos(), species.getYPos(), getRange());
	}
	
	public boolean isSeeing(ThingInfo thing) {
		return getThingsAtSight().contains(thing);
	}
	
	public short getRange() {
		return (short)(species.getAgingMultiplierAscDesc()*species.getGenetics().getSenseSight()*MAX_SIGHT_RANGE);
	}
	
	public boolean isEffective() {
		return (species.getGenetics().getSenseSight() > 0.3);
	}

}
