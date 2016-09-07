package br.thejungle.species.abilities;

import java.io.Serializable;

import br.thejungle.species.Species;

/**
 * This is an abstract class for all abilities
 */
public abstract class Ability implements Serializable {

    protected Species species;
    
    public Ability(Species species) {
        this.species = species;
    }

    public abstract void perform();

    public abstract boolean isEffective();

}
