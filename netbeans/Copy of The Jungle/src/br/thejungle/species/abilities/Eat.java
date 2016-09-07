package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.environment.things.info.FoodInfo;
import br.thejungle.species.Species;

/**
 * Here is implemented the ability of eating
 */
public class Eat extends Ability implements Task {

    /**
     * @param species
     */
    public Eat(Species species) {
        super(species);
    }

    /**
     * Tries to eat whatever is on current position. This method is syncronous.
     */
    public void perform(short qtty) {
    }

    /**
     * Goes to the food position and then tries to eat. This method is
     * assyncronous.
     */
    public void perform(FoodInfo food, short qtty) {
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
        return false;
   }

}
