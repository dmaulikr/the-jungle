package br.thejungle.environment;

import br.thejungle.environment.things.Meat;
import br.thejungle.environment.things.Plant;
import java.io.Serializable;
import java.util.Map;

import br.thejungle.environment.things.Thing;
import br.thejungle.species.Species;
import br.thejungle.species.SpeciesGroup;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * This is the jungle. It is responsible for performing various task such as
 * aging species, aging food and performing long time tasks from species
 * (walking etc).
 */
public class Jungle implements Runnable, Serializable {
    
    private ConcurrentMap<String, CopyOnWriteArrayList<Task>> tasks;
    private ConcurrentMap<String, SpeciesGroup> speciesGroups;
    private String jungleName;
    private float mutationRate;
    private boolean active;
    private long width, height;
    private static Random random = new Random();
    private static Logger logger = Logger.getLogger("br.thejungle.environment.Jungle");
    private short maxNumberOfPlants;

    private CopyOnWriteArrayList<Plant> plants;
    private CopyOnWriteArrayList<Meat> meats;

    /**
     * Initialize the new jungle
     */
    public Jungle(String jungleName, long width, long height, float mutationRate) {
        this.tasks = new ConcurrentHashMap<String, CopyOnWriteArrayList<Task>>();
        this.plants = new CopyOnWriteArrayList<Plant>();
        this.meats = new CopyOnWriteArrayList<Meat>();
        this.speciesGroups = new ConcurrentHashMap<String, SpeciesGroup>();
        this.active = false;
        this.mutationRate = mutationRate;
        this.jungleName = jungleName;
        this.width = width;
        this.height = height;
        this.maxNumberOfPlants = 10;
    }
    
    
    protected void foodCare() {
        //care about plants
        
        //create new plants
        if(plants.size()<maxNumberOfPlants) {
            createRandomPlants(maxNumberOfPlants - plants.size());
        }
        
        //treat existing plants
        for(Plant plant: plants) {

            //step grow plant
            plant.timeElapsed();
            
            //remove exausted plants
            if(!plant.isValid()) {
                plants.remove(plant);
                logger.fine("Plant removed\n" + plant);
            }
        }

        //care about meats
        for(Meat meat: meats) {

            meat.timeElapsed();
            
            //remove exausted meats
            if(!meat.isValid()) {
                meats.remove(meat);
                logger.fine("Meat removed\n" + meat);
            }
        }
}
    
    private void createRandomPlants(int numberOfPlants) {
        for(int i=0; i<numberOfPlants; i++) {
            Plant plant = new Plant((short)random.nextInt(200), (short)random.nextInt(3), random.nextInt(10000));
            putThingAtRandomPlace(plant);
            logger.fine("New plant created\n" + plant);
        }
    }
    
    protected void speciesCare() {
        for(SpeciesGroup sg: speciesGroups.values()) {
            sg.timeElapsed();
        }
    }

    /**
     * Get each species's tasks, get the current task for the species and stepWork it
     * A species may submit multiple tasks, but they will be executed one by one
     */
    protected void tasksCare() {
        for(String key: tasks.keySet()) {
            //get current task for this species and stepWork it
            CopyOnWriteArrayList<Task> stasks = tasks.get(key);
            Task task = stasks.get(0);
            task.stepWork();
            if(task.isCompleted()) {
                stasks.remove(task);
                logger.info("Task " + task.getClass().getName() + " for " + task.getIndividualID() + " was completed");
            }
        }
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        
        //activate species groups and create threads for each group
        for(SpeciesGroup sg: speciesGroups.values()) {
            runSpeciesGroup(sg);
        }
        
        //performs modifications to the jungle
        while(active) {
            foodCare();
            speciesCare();
            tasksCare();
        }
        
        //deactivate species groups
        for(SpeciesGroup sg: speciesGroups.values()) {
            sg.setActive(false);
        }
        
    }
    
    private SpeciesGroup getSpeciesGroup(Species species) {
        //Try to get a species group for this species
        SpeciesGroup sg = speciesGroups.get(species.getSpeciesName());
        
        //Create an start a new SpeciesGroup
        if(sg==null) {
            sg = new SpeciesGroup(species.getSpeciesName(), this);
            speciesGroups.put(species.getSpeciesName(), sg);
            runSpeciesGroup(sg);
        }
        
        return sg;
    }
    
    private void runSpeciesGroup(SpeciesGroup sg) {
        sg.setActive(true);
        new Thread(sg).start();
    }
    
    public void executeTask(Task task) {
        CopyOnWriteArrayList<Task> t = tasks.get(task.getIndividualID());
        t.add(task);
    }
    
    public CopyOnWriteArrayList getThings(int x, int y, int radius) {
        CopyOnWriteArrayList<Thing> things = new CopyOnWriteArrayList<Thing>();
        //look for plants
        for(Thing t: plants) {
            if(isThingInRadius(t, x, y, radius)) things.add(t);
        }
        //look for meats
        for(Thing t: meats) {
            if(isThingInRadius(t, x, y, radius)) things.add(t);
        }
        //look for species
        for(Thing t: meats) {
            if(isThingInRadius(t, x, y, radius)) things.add(t);
        }
        return things;
    }
    
    public boolean isThingInRadius(Thing thing, int x, int y, int radius) {
        //TODO
        return false;
    }
    
    public CopyOnWriteArrayList<Species> getSpecies() {
        CopyOnWriteArrayList<Species> species = new CopyOnWriteArrayList<Species>();
        for(SpeciesGroup sg: speciesGroups.values()) {
            species.addAll(sg.getSpecies());
        }
        return species;
    }
    
    public Map getTasks() {
        return tasks;
    }
    
    public void acceptSpecies(Species species) {
        SpeciesGroup sg = getSpeciesGroup(species);
        sg.addSpecies(species);
        putThingAtRandomPlace(species);
    }
    
    public void removeSpecies(Species species) {
        SpeciesGroup sg = getSpeciesGroup(species);
        sg.removeSpecies(species);
    }
    
    public void removeSpecies(String speciesName) {
        for(SpeciesGroup sg: speciesGroups.values()) {
            sg.removeSpecies(speciesName);
        }
    }
    
    public void removeAllSpecies() {
        for(SpeciesGroup sg: speciesGroups.values()) {
            sg.removeAllSpecies();
        }
    }
    
    /**
     * @return Returns the active.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public long countSpecies() {
        long cs = 0;
        for(SpeciesGroup sg: speciesGroups.values()) {
            cs += sg.countSpecies();
        }
        return cs;
    }
    
    public long getTotalFoodRichness() {
        long fr = 0;
        for(Meat meat: meats) {
            fr += meat.getRichness();
        }
        for(Plant plant: plants) {
            fr += plant.getRichness();
        }
        return fr;
    }
    
    protected void putThingAtRandomPlace(Thing thing) {
        thing.setXPos((long)(width*random.nextFloat()));
        thing.setYPos((long)(height*random.nextFloat()));
    }
    
    public String toString() {
        String str = jungleName + "\n";
        str += "    Mutation rate: " + mutationRate + "\n";
        str += "    SpeciesGroups: " + speciesGroups.size() + "\n";
        str += "    Species: " + countSpecies() + "\n";
        return str;
    }
    
}
