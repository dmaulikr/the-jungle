package br.thejungle.species;

import br.thejungle.environment.things.info.ThingInfo;

/**
 * This is a Species wrapper class for exposing only the desired propertics to
 * the public. We couldn't use just an interface because experts would discover
 * this and use the Species class directly (being able to manipulate their
 * properties).
 */
public class SpeciesInfo extends ThingInfo {

    private Species species;
    
    public SpeciesInfo(Species species) {
        super(species);
        this.species = species;
    }

    public String getSpeciesName() {
        return species.getSpeciesName();
    }

    public String getIndividualID() {
        return species.getIndividualID();
    }

    public float getHealth() {
        return species.getHealth();
    }

}
