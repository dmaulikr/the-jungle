package br.thejungle.environment.things.info;

import java.io.Serializable;
import br.thejungle.environment.things.Thing;

/**
 * This is an abstract class for all ThingInfos
 */
public abstract class ThingInfo implements Serializable {

    private Thing thing;
    
    public ThingInfo(Thing thing) {
        this.thing = thing;
    }
    
    public long getXPos() {
        return thing.getXPos();
    }

    public long getYPos() {
        return thing.getYPos();
    }

    public long getZPos() {
        return thing.getZPos();
    }
}
