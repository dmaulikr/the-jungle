package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.species.Species;

/**
 * Here is implemented the ability of walking
 */
public class Walk extends Ability {

    public static short DEFAULT_SPEED = 10;
    
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
        gotoPos(x, y, DEFAULT_SPEED);
    }

    /**
     * Walk to a desired point in the jungle at certain speed. This method is
     * assyncronous.
     */
    public void gotoPos(long x, long y, short speed) {
        Task t = new WalkTask(species, x, y, speed);
        species.getJungle().executeTask(t);
    }

    public void perform(float directionAngle, long distance, short speed) {
        Task t = new WalkTask(species, directionAngle, distance, speed);
        species.getJungle().executeTask(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#getIndividualID()
     */
    public String getIndividualID() {
        return species.getIndividualID();
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
        return species.getGenetics().getAbilityWalk()>=0.3;
    }

}
