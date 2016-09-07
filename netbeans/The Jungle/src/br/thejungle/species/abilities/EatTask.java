/*
 * EatTask.java
 *
 * Created on 6 de Dezembro de 2004, 23:09
 */

package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.environment.things.FoodInfo;
import br.thejungle.species.Species;

/**
 *
 * @author Flávio
 */
public class EatTask implements Task {
    
    private Species species;
    private short qtty;
    private boolean hasEatten;
        
    /** Creates a new instance of EatTask */
    public EatTask(Species species, short qtty) {
        this.species = species;
        this.qtty = qtty;
        this.hasEatten = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#stepWork()
     */
    public void stepWork() {
        species.getEatAbility().perform(qtty);
        hasEatten = true;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#isCompleted()
     */
    public boolean isCompleted() {
        return hasEatten;
    }
    
    public String getIndividualID() {
        return species.getIndividualID();
    }
    
}
