package br.thejungle.environment;

import br.thejungle.species.SpeciesInfo;

/**
 * This interface is implemented by any class that requires various ticks for
 * completing its task. Classes that implements this interface may be submitted
 * to the Jungle for execution
 */
public interface Task {

	public abstract SpeciesInfo getSpeciesInfo();

	/**
	 * This method is invoked by the Jungle at each time step
	 */
	public abstract void stepWork();

	/**
	 * This indicates to the jungle that this task is completed
	 */
	public abstract boolean isCompleted();

	public abstract boolean wasSuccessful();
	
}
