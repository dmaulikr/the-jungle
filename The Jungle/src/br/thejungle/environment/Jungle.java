package br.thejungle.environment;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import br.thejungle.environment.things.Meat;
import br.thejungle.environment.things.Plant;
import br.thejungle.environment.things.Thing;
import br.thejungle.exceptions.SpeciesNotFoundException;
import br.thejungle.species.Species;
import br.thejungle.species.SpeciesBrain;
import br.thejungle.species.SpeciesGroup;
import br.thejungle.species.SpeciesInfo;
import br.thejungle.util.MathUtil;

/**
 * This is the jungle. It is responsible for performing various task such as
 * aging species, aging food and performing long time tasks from species
 * (walking etc).
 */
public class Jungle implements Runnable, Serializable {

	private ConcurrentMap<SpeciesBrain, ConcurrentLinkedQueue<Task>> tasks;
	private ConcurrentMap<String, SpeciesGroup> speciesGroups;
	private String jungleName;
	private float mutationRate;
	private boolean active;
	private long width, height;
	private short maxNumberOfPlants;
	transient private static ThreadPoolExecutor threadPool = null;
	transient private static Random random = new Random();
	private static Logger logger = Logger.getLogger("br.thejungle.environment.Jungle");

	private CopyOnWriteArrayList<Plant> plants;
	private CopyOnWriteArrayList<Meat> meats;

	public static final int MAX_NUMBER_OF_PLANTS = 3;
    public static final int MAX_JUNGLES = 15;
    public static final int MAX_TASKS_IN_QUEUE_PER_INDIVIDUAL = 5;
    public static final int MAX_PLANT_RICHNESS = 100;

	/**
	 * Initialize the new jungle
	 */
	public Jungle(String jungleName, long width, long height, float mutationRate) {
		this.tasks = new ConcurrentHashMap<SpeciesBrain, ConcurrentLinkedQueue<Task>>();
		this.plants = new CopyOnWriteArrayList<Plant>();
		this.meats = new CopyOnWriteArrayList<Meat>();
		this.speciesGroups = new ConcurrentHashMap<String, SpeciesGroup>();
		this.active = false;
		this.mutationRate = mutationRate;
		this.jungleName = jungleName;
		this.width = width;
		this.height = height;
		this.maxNumberOfPlants = MAX_NUMBER_OF_PLANTS;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// performs modifications to the jungle
		while (active) {
//System.out.println("CICLE");
			foodCare();
			speciesCare();
			tasksCare();
			try {
	            Thread.sleep(Nature.THREAD_SLEEP_TIME_PER_CICLE);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public void startup() {
		
		// if this jungle is already active, do nothing
		if(active) return;
		
		// activate species groups and create threads for each group
		active = true;
		getThreadPool().execute(this);

		for (SpeciesGroup sg : speciesGroups.values()) {
			sg.startup();
		}
		
		logger.info("Jungle " + jungleName + " startup");
	}
	
	public void shutdown() {
		// if this jungle is already inactive, do nothing
		if(!active) return;

		// shutdown species groups
		for (SpeciesGroup sg : speciesGroups.values()) {
			sg.shutdown();
		}
		
		// wait for the jungle thread to finish current tasks
		active = false;
		try {
			getThreadPool().shutdown();
			getThreadPool().awaitTermination(Nature.THREAD_TERMINATION_TIMEOUT,
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.severe("Couldn't wait for jungle to finish tasks: "
					+ e.getMessage());
		}

		logger.info("Jungle " + jungleName + " shutdown");
	}

	protected void foodCare() {
		long t = System.currentTimeMillis();
		// care about plants

		// create new plants
		if (plants.size() < maxNumberOfPlants) {
			createRandomPlants(maxNumberOfPlants - plants.size());
		}

		// treat existing plants
		for (Plant plant : plants) {

			// step grow plant
			plant.timeElapsed();

			// remove exausted plants
			if (!plant.isValid()) {
				plants.remove(plant);
				logger.finer("Plant removed\n" + plant);
			}
		}

		// care about meats
		for (Meat meat : meats) {

			meat.timeElapsed();

			// remove exausted meats
			if (!meat.isValid()) {
				meats.remove(meat);
				logger.finer("Meat removed\n" + meat);
			}
		}
		logger.finest("Jungle " + jungleName + " spent " + (System.currentTimeMillis()-t) + "ms in foodCare");
	}

	private void createRandomPlants(int numberOfPlants) {
		for (int i = 0; i < numberOfPlants; i++) {
			Plant plant = new Plant((short) random.nextInt(MAX_PLANT_RICHNESS), (short)(random.nextInt(10)+1), random.nextInt(10000));
			putThingAtRandomPlace(plant);
			plants.add(plant);
			logger.finer("New plant created\n" + plant);
		}
	}

	protected void speciesCare() {
		long t = System.currentTimeMillis();
		for (SpeciesGroup sg : speciesGroups.values()) {
			sg.timeElapsed();
		}
		logger.finest("Jungle " + jungleName + " spent " + (System.currentTimeMillis()-t) + "ms in speciesCare");
	}

	/**
	 * Get each species's tasks, get the current task for the species and
	 * stepWork it A species may submit multiple tasks, but they will be
	 * executed one by one
	 */
	protected void tasksCare() {
		long t = System.currentTimeMillis();
		for (SpeciesBrain speciesBrain: tasks.keySet()) {
			// get current task for this species and stepWork it
		    ConcurrentLinkedQueue<Task> stasks = tasks.get(speciesBrain);
		    /*String s = "";
		    for(Task ta: stasks) {
		        s += ta;
		    }
		    System.out.println(s);*/
		    if(stasks.size()==0) return;
			Task task = stasks.peek();
			// if this species is dead, remove its tasks from queue
			if(!speciesBrain.isValid()) {
				tasks.remove(speciesBrain);
				return;
			}
			
			logger.finest("Step working task " + task);
			task.stepWork();
			
			if (task.isCompleted()) {
			    speciesBrain.taskPerformed(task);
			    stasks.remove(task);
				logger.finer("Task " + task.getClass().getName() + " for "
						+ task.getSpeciesInfo().getIndividualID() + " was completed. " + task);
			}
		}
		logger.finest("Jungle " + jungleName + " spent " + (System.currentTimeMillis()-t) + "ms in tasksCare");
	}

	private SpeciesGroup getSpeciesGroup(Species species) {
		// Try to get a species group for this species
		SpeciesGroup sg = speciesGroups.get(species.getSpeciesName());

		// Create an start a new SpeciesGroup
		if (sg == null) {
			sg = new SpeciesGroup(species.getSpeciesName(), this);
			speciesGroups.put(species.getSpeciesName(), sg);
			sg.startup();
		}

		return sg;
	}

	public void executeTask(SpeciesBrain speciesBrain, Task task) {
		if (task == null) return;
		ConcurrentLinkedQueue<Task> stasks = tasks.get(speciesBrain);
	    if(stasks==null) {
	        stasks = new ConcurrentLinkedQueue<Task>();
	        tasks.put(speciesBrain, stasks);
	    }
		stasks.add(task);
		String s = "";
	    for(Task ta: stasks) {
	        s += ta + ", " ;
	    }
		logger.finer("Task " + task + " was put in queue to be executed");
		logger.finest("Current queue: " + s);

		if(stasks.size()>MAX_TASKS_IN_QUEUE_PER_INDIVIDUAL) {
		    stasks.poll();
			logger.fine("Max tasks per individual reached. A task was removed from queue without being executed.");
		}
	}

	public CopyOnWriteArrayList<Thing> getThings(float x, float y, float radius) {
		CopyOnWriteArrayList<Thing> things = new CopyOnWriteArrayList<Thing>();

		for(Thing thing: getAllThings()) {
			if (MathUtil.isPointInCircle(x, y, radius, thing.getXPos(), thing.getYPos()))
				things.add(thing);
		}
		
		return things;
	}
	
	public CopyOnWriteArrayList<Thing> getAllThings() {
		CopyOnWriteArrayList<Thing> things = new CopyOnWriteArrayList<Thing>();
		
		things.addAll(getSpecies());
		things.addAll(plants);
		things.addAll(meats);
		
	    return things;
	}

	public CopyOnWriteArrayList<Species> getSpecies() {
		CopyOnWriteArrayList<Species> species = new CopyOnWriteArrayList<Species>();
		for (SpeciesGroup sg : speciesGroups.values()) {
			species.addAll(sg.getSpecies());
		}
		return species;
	}

	public Map<SpeciesBrain, ConcurrentLinkedQueue<Task>> getTasks() {
		return tasks;
	}

	public void acceptSpecies(Species species) {
		SpeciesGroup sg = getSpeciesGroup(species);
		sg.addSpecies(species);
		putThingAtRandomPlace(species);
		logger.fine("Species " + species.getIndividualID() + " accepted");
	}

	public void acceptSpecies(Species species, long xPos, long yPos) {
		SpeciesGroup sg = getSpeciesGroup(species);
		species.setXPos(xPos);
		species.setYPos(yPos);
		sg.addSpecies(species);
		logger.fine("Species " + species.getIndividualID() + " accepted");
	}

	public void removeSpecies(Species species) {
		SpeciesGroup sg = getSpeciesGroup(species);
		sg.removeSpecies(species);
		logger.fine("Species " + species.getIndividualID() + " removed");
	}

	public void removeSpecies(String speciesName) {
		for (SpeciesGroup sg : speciesGroups.values()) {
			sg.removeSpecies(speciesName);
		}
	}

	public void removeAllSpecies() {
		for (SpeciesGroup sg : speciesGroups.values()) {
			sg.removeAllSpecies();
		}
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	public long countSpecies() {
		long cs = 0;
		for (SpeciesGroup sg : speciesGroups.values()) {
			cs += sg.countSpecies();
		}
		return cs;
	}

	public long countMeats() {
		return meats.size();
	}

	public long countPlants() {
		return plants.size();
	}

	public long getTotalFoodRichness() {
		long fr = 0;
		for (Meat meat : meats) {
			fr += meat.getRichness();
		}
		for (Plant plant : plants) {
			fr += plant.getRichness();
		}
		return fr;
	}

	protected void putThingAtRandomPlace(Thing thing) {
		thing.setXPos((long) (width * random.nextFloat()));
		thing.setYPos((long) (height * random.nextFloat()));
	}

	public String toString() {
		String str = jungleName + "\n";
		str += "    Mutation rate: " + mutationRate + "\n";
		str += "    SpeciesGroups: " + speciesGroups.size() + "\n";
		str += "    Species: " + countSpecies() + "\n";
		return str;
	}

	public String getJungleName() {
		return jungleName;
	}

	private ThreadPoolExecutor getThreadPool() {
		if(threadPool==null) {
			threadPool = new ThreadPoolExecutor(1, MAX_JUNGLES,
					Nature.THREAD_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(MAX_JUNGLES));
		}
		return threadPool;
	}
	
	public Species findSpeciesByInfo(SpeciesInfo speciesInfo) throws SpeciesNotFoundException {
        CopyOnWriteArrayList<Species> allSpecies = getSpecies();
        for(Species s: allSpecies) {
            if(s.getIndividualID().equals(speciesInfo.getIndividualID())) 
                return s;
        }
        throw new SpeciesNotFoundException("Species for SpeciesInfo " + speciesInfo + " not found. IndividualID=" + speciesInfo.getIndividualID());
	}

    public float getMutationRate() {
        return mutationRate;
    }
	
	public long getHeight() {
		return height;
	}
	public long getWidth() {
		return width;
	}
}
