package br.thejungle.species.senses;

import br.thejungle.environment.things.Thing;
import br.thejungle.environment.things.info.ThingInfo;
import br.thejungle.species.SenseEvent;

/**
 * This is an event for the Sigtht sense and is used for informing the Species
 * Brain about anything that was seen.
 */
public class SightEvent extends SenseEvent {

	private ThingInfo thingInfo;
	private Thing thing;

	public SightEvent(Thing thing) {
		this.thingInfo = thing.getThingInfo();
		this.thing = thing;
	}

	/**
	 * @return Returns the thing.
	 */
	public ThingInfo getThingInfo() {
		return thingInfo;
	}
	
	private Thing getThing() {
		return thing;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SightEvent) {
			SightEvent se = (SightEvent)obj;
			return (se.getThing()==getThing() &&
					se.getThingInfo().getXPos()==getThingInfo().getXPos() &&
					se.getThingInfo().getYPos()==getThingInfo().getYPos() &&
					se.getThingInfo().getZPos()==getThingInfo().getZPos());
		}
		return false;
	}
	
}
