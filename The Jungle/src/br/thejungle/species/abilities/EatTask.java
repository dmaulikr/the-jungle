/*
 * Created on 11/12/2004
 */
package br.thejungle.species.abilities;

import br.thejungle.environment.TaskSupport;
import br.thejungle.species.Species;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class EatTask extends TaskSupport {

    private short qtty;
    
    /**
     * Constructor
     */
    public EatTask(Species species, short qtty) {
        super(species);
        this.qtty = qtty;
    }

    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#stepWork()
     */
    public void stepWork() {
        species.getAbilityEat().perform(qtty);
        completed = true;
        successful = true;
    }

}
