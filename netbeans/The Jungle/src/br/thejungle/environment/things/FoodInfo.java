package br.thejungle.environment.things;

import br.thejungle.species.SpeciesInfo;

/**
 * This is a wrapper class for exposing only desired properties of type Food
 */
public class FoodInfo extends ThingInfo {

    /**
     * @param thing
     */
    public FoodInfo(Thing thing) {
        super(thing);
    }

    public short TYPE_PLANT = 1;

    public short TYPE_MEAT = 2;

    private Food food;

    public short getRichness() {
        return 0;
    }

    public short getSize() {
        return 0;
    }

    public short getType() {
        return 0;
    }

    public SpeciesInfo getSpecies() {
        return null;
    }

}
