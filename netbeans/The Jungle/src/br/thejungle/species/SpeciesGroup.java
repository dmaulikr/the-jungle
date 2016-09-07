package br.thejungle.species;

import java.io.Serializable;

import br.thejungle.environment.Jungle;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class SpeciesGroup implements Runnable, Serializable {
    
    private String groupName;
    private CopyOnWriteArrayList<Species> lspecies;
    private Jungle jungle;
    private boolean active;
    private static Logger logger = Logger.getLogger("br.thejungle.species.SpeciesGroup");
    private static Random random = new Random();
    
    public SpeciesGroup(String groupName, Jungle jungle) {
        this.groupName = groupName;
        this.lspecies = new CopyOnWriteArrayList<Species>();
        this.jungle = jungle;
    }
    
    /**
     * @see java.lang.Runnable#run()
     * This thread is used by a whole group of Species.
     */
    public void run() {
        while(active) {
            for(Species species: lspecies) {
                species.getSpeciesBrain().think();
            }
        }
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
        for(Species species: lspecies) {
            if(species.getSpeciesName().equals(speciesName)) {
                removeSpecies(species);
            }
        }
    }
    
    public void removeAllSpecies() {
        for(Species species: lspecies) {
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
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void timeElapsed() {
        for(Species species: lspecies) {
            species.timeElapsed();
            
            //remove the dead
            if(!species.isValid()) {
                removeSpecies(species);
                logger.info(species.getIndividualID() + " is dead!");
            }
        }
    }

}

