/*
 * Created on 12/12/2004
 */
package br.thejungle.environment;

import br.thejungle.species.Species;
import br.thejungle.species.SpeciesInfo;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public abstract class TaskSupport implements Task {

    protected Species species;
    protected boolean completed;
    protected boolean successful;
    
    public TaskSupport(Species species) {
        this.species = species;
        this.completed = false;
        this.successful = false;
    }
    
    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#stepWork()
     */
    public abstract void stepWork();
    
    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#getSpeciesInfo()
     */
    public SpeciesInfo getSpeciesInfo() {
        return species.getSpeciesInfo();
    }

    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#isCompleted()
     */
    public boolean isCompleted() {
        return completed;
    }

    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#wasSuccessful()
     */
    public boolean wasSuccessful() {
        return successful;
    }
    
    public String toString() {
    	return "species="+ species.getIndividualID() +"; completed="+completed+"; successful="+successful;
    }

}
