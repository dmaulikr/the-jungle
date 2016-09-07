package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.species.Species;

public class TaskControl extends Ability {

    /**
     * @param species
     */
    public TaskControl(Species species) {
        super(species);
    }

    public void cancelAllTasks() {
    }

    public void cancelCurrentTask() {
    }

    public void cancelTask(Task task) {
    }

    public Task getCurrentTask() {
        return null;
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
