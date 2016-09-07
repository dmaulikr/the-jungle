package br.thejungle.species.senses;

import br.thejungle.species.Species;


/**
 * This is the Sight sense. Here is implemented how the Species can see
 * anything.
 */
public class Sight extends Sense {

    public Sight(Species species) {
        super(species);
    }

    public void work() {
    }

    public boolean isEffective() {
        return (species.getGenetics().getSenseSight()>0.3);
    }

}
