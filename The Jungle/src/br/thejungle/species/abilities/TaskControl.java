package br.thejungle.species.abilities;

import java.util.concurrent.ConcurrentLinkedQueue;

import br.thejungle.environment.Task;
import br.thejungle.species.Species;

public class TaskControl extends Ability {

    ConcurrentLinkedQueue<Task> tasks;
    
	/**
	 * @param species
	 */
	public TaskControl(Species species) {
		super(species);
	}
	
	private void updateTasks() {
	    tasks = species.getJungle().getTasks().get(species.getSpeciesBrain());
	}

	public void cancelAllTasks() {
	    updateTasks();
	    tasks.clear();
	}

	public void cancelCurrentTask() {
	    updateTasks();
	    tasks.poll();
	}

	public void cancelTask(Task task) {
	    updateTasks();
	    tasks.remove(task);
	}

	public Task getCurrentTask() {
	    updateTasks();
	    return tasks.peek();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#perform()
	 */
	public boolean perform() {
	   return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#isEffective()
	 */
	public boolean isEffective() {
		return true;
	}

}
