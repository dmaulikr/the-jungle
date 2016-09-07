package br.thejungle.species;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import br.thejungle.environment.Jungle;
import br.thejungle.environment.Nature;
import br.thejungle.species.senses.Sense;

public class SpeciesGroup implements Runnable, Serializable {

	private String groupName;
	private CopyOnWriteArrayList<Species> lspecies;
	private Jungle jungle;
	private boolean active;

	transient private static Random random = new Random();
	transient private static Logger logger = Logger.getLogger("br.thejungle.species.SpeciesGroup");
	transient private static ThreadPoolExecutor threadPool = null;
	
	public static final int MAX_SPECIES_GROUPS = 128;

	public SpeciesGroup(String groupName, Jungle jungle) {
		this.groupName = groupName;
		this.lspecies = new CopyOnWriteArrayList<Species>();
		this.jungle = jungle;
	}

	/**
	 * @see java.lang.Runnable#run() This thread is used by a whole group of
	 *      Species.
	 */
	public void run() {
		while (active) {
			for (Species species : lspecies) {
				long t = System.currentTimeMillis();
				species.getSpeciesBrain().think();
				logger.finest("Species " + species.getIndividualID() + " spent " + (System.currentTimeMillis()-t) + "ms thinking");
			}
			try {
	            Thread.sleep(Nature.THREAD_SLEEP_TIME_PER_CICLE);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public void startup() {
		// if this speciesGroup is already active, do nothing
		if(active) return;
		
		active = true;
		getThreadPool().execute(this);
		logger.fine("SpeciesGroup " + groupName + " startup");
	}
	
	public void shutdown() {
		// if this speciesGroup is already inactive, do nothing
		if(!active) return;
		
        // wait for the speciesGroup thread to finish all current tasks
		active = false;
        try {
            getThreadPool().shutdown();
            getThreadPool().awaitTermination(Nature.THREAD_TERMINATION_TIMEOUT,
                    TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.severe("Couldn't wait for the speciesGroup to finish their tasks: "
                            + e.getMessage());
        }
        
		logger.fine("SpeciesGroup " + groupName + " shutdown");
	}
	
	private ThreadPoolExecutor getThreadPool() {
		if(threadPool==null) {
			threadPool = new ThreadPoolExecutor(1, MAX_SPECIES_GROUPS,
					Nature.THREAD_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(MAX_SPECIES_GROUPS));
		}
		return threadPool;
	}

	public void addSpecies(Species species) {
		lspecies.add(species);
		species.setJungle(jungle);
	}

	public CopyOnWriteArrayList<Species> getSpecies() {
		return lspecies;
	}

	public boolean removeSpecies(Species species) {
		species.setJungle(null);
		return lspecies.remove(species);
	}

	public void removeSpecies(String speciesName) {
		for (Species species : lspecies) {
			if (species.getSpeciesName().equals(speciesName)) {
				removeSpecies(species);
			}
		}
	}

	public void removeAllSpecies() {
		for (Species species : lspecies) {
			removeSpecies(species);
		}
	}

	public int countSpecies() {
		return lspecies.size();
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            The active to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public void timeElapsed() {
		for (Species species : lspecies) {
			species.timeElapsed();

			// remove the dead
			if (!species.isValid()) {
				removeSpecies(species);
				logger.info(species.getIndividualID() + " is dead!");
			
			} else {
			
				// make senses work
				for(Sense sense: species.getSenses()) {
					sense.work();
				}
			}
		}
	}

}
