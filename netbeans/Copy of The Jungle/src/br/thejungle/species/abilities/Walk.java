package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.species.Species;

/**
 * Here is implemented the ability of walking
 */
public class Walk extends Ability implements Task {

    /**
     * @param species
     */
    public Walk(Species species) {
        super(species);
    }

    /**
     * Walk to a desired point in the jungle. The velocity will be the max
     * allowed. This method is assyncronous.
     */
    public void gotoPos(long x, long y) {
    }

    /**
     * Walk to a desired point in the jungle at certain speed. This method is
     * assyncronous.
     */
    public void gotoPos(long x, long y, short speed) {
    }

    public void perform(short directionAngle, long distance) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#getIndividualID()
     */
    public String getIndividualID() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#stepWork()
     */
    public void stepWork() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#stepWork(long)
     */
    public void stepWork(long timeout) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#isCompleted()
     */
    public boolean isCompleted() {
        return false;
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#perform()
     */
    public void perform() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#isEffective()
     */
    public boolean isEffective() {
        // TODO Auto-generated method stub
        return false;
    }

}
