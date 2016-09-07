package br.thejungle.species.abilities;

import br.thejungle.species.Species;
import br.thejungle.species.SpeciesInfo;

/**
 * Here is implemented the ability of reproducting
 */
public class Reproduce extends Ability {

    /**
     * @param species
     */
    public Reproduce(Species species) {
        super(species);
    }

    /**
     * Reproduces with an partner. This method is syncronous.
     */
    public void perform(SpeciesInfo partner) {
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#perform()
     */
    public void perform() {
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#isEffective()
     */
    public boolean isEffective() {
        // TODO Auto-generated method stub
        return false;
    }

}
