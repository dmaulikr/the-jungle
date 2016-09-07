package br.thejungle.species.senses;

import br.thejungle.environment.things.Thing;

/**
 * This is an event for the Sigtht sense and is used for informing the Species
 * Brain about anything that was seen.
 */
public class SightEvent extends SenseEvent {

    private Thing thing;
    
    public SightEvent(Thing thing) {
        this.thing = thing;
    }

    /**
     * @return Returns the thing.
     */
    public Thing getThing() {
        return thing;
    }
}
