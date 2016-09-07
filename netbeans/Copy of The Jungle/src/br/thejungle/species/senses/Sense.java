package br.thejungle.species.senses;

import java.io.Serializable;

import br.thejungle.species.Species;

/**
 * This is as abstract class for all Senses
 */
public abstract class Sense implements Serializable {

    protected Species species;

    public Sense(Species species) {
        this.species = species;
    }
    
    public abstract void work();
    
    public abstract boolean isEffective();

}
