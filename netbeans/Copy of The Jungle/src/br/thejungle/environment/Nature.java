package br.thejungle.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import br.thejungle.exceptions.BrainNotInstantiableException;
import br.thejungle.exceptions.DuplicateJungleException;
import br.thejungle.exceptions.JungleNotFoundException;
import br.thejungle.species.Species;
import br.thejungle.species.SpeciesBrain;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;



/**
 * This is the jungle's portal to the World.
 * The Nature represents all jungles states, so it must be able to persist itself at shutdown and recover the last state on startup.
 */
public class Nature implements Serializable {
    
    private transient static final String SAVE_FILE_NAME = "Nature.ser";
    private transient static Nature instance = null;
    private ConcurrentMap<String, Jungle> jungles;
    private int timesShutDown;
    private Date creationDate;
    private static Logger logger = Logger.getLogger("br.thejungle.environment.Nature");
    
    /**
     * Singleton pattern implementation
     */
    private Nature() {
        jungles = new ConcurrentHashMap<String, Jungle>();
        creationDate = Calendar.getInstance().getTime();
        logger = Logger.getLogger(getClass().getName());
    }
    
    public static Nature getInstance() {
        if(instance==null) {
            try {
                instance = restoreLastState();
            } catch (Exception e) {
                System.err.println("Error on deserialization of last Nature state. " + e.getMessage());
            }
        }
        if(instance==null) instance = new Nature();
        return instance;
    }
    
    public void createJungle(String jungleName, long width, long height, float mutationRate) throws DuplicateJungleException {
        if(jungles.get(jungleName)!=null) throw new DuplicateJungleException("Jungle named " + jungleName + " already exists");
        Jungle jungle = new Jungle(jungleName, width, height, mutationRate);
        jungles.put(jungleName, jungle);
        runJungle(jungle);
    }
    
    private void runJungle(Jungle jungle) {
        jungle.setActive(true);
        new Thread(jungle).start();
    }
    
    public void removeJungle(String jungleName) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungles.remove(jungleName);
        jungle.setActive(false);
    }
    
    public void createSpecies(String jungleName, Developer developer, Class speciesBrain) throws JungleNotFoundException, BrainNotInstantiableException {
        try {
            
            Jungle jungle = getJungle(jungleName);
            SpeciesBrain brain = (SpeciesBrain)speciesBrain.newInstance();
            Species species = new Species(developer, brain);
            brain.setSpecies(species);
            jungle.acceptSpecies(species);
            
        } catch (InstantiationException e) {
            throw new BrainNotInstantiableException("The SpeciesBrain " + speciesBrain.getClass().getName() + " couldn't be instantiated", e);
        } catch (IllegalAccessException e) {
            throw new BrainNotInstantiableException("The SpeciesBrain " + speciesBrain.getClass().getName() + " couldn't be instantiated", e);
        }
    }
    
    public void acceptSpecies(String jungleName, Species species) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.acceptSpecies(species);
    }
    
    public void removeSpecies(String jungleName, String speciesName) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.removeSpecies(speciesName);
    }
    
    public void removeSpecies(String speciesName) {
        for(Jungle j: jungles.values()) {
            j.removeSpecies(speciesName);
        }
    }
    
    public void removeAllSpecies(String jungleName) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.removeAllSpecies();
    }
    
    private Jungle getJungle(String jungleName) throws JungleNotFoundException {
        Jungle jungle = jungles.get(jungleName);
        if(jungle==null) throw new JungleNotFoundException("Jungle named " + jungleName + " not found in this Nature");
        return jungle;
    }
    
    /**
     * Performs the persistence of self state. Persisting the self state will make all jungles and agregates alive on disk! This class is always a singleton from a previous saved state.
     * @throws IOException
     */
    public void saveCurrentState() throws IOException {
        File file = new File(SAVE_FILE_NAME);
        if(file.exists()) file.delete();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }
    
    public static Nature restoreLastState() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(SAVE_FILE_NAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        if(instance!=null) instance.shutdown();
        Nature ninstance = (Nature)ois.readObject();
        ninstance.startup();
        
        ois.close();
        
        return ninstance;
    }
    
    /**
     * Mark the jungles as actives and create threads for them
     */
    public void startup() {
        for(Jungle j: jungles.values()) {
            runJungle(j);
        }
    }
    
    /**
     * Mark the jungles as inactives
     */
    public void shutdown() {
        for(Jungle j: jungles.values()) {
            j.setActive(false);
        }
        timesShutDown++;
    }
    
    /**
     *Calls the saveCurrentState() method to save the current state before being garbage collected.
     * @throws IOException
     */
    public void finalize() throws IOException {
        this.shutdown();
        this.saveCurrentState();
    }
    
    public Map getJungles() {
        return jungles;
    }
    
    public String toString() {
        int cs = 0;
        String str = "Nature alive since " + creationDate + "\n\n";
        str += "Shutdown times: " + timesShutDown + "\n";
        str += "Jungles: " + jungles.values().size() + "\n\n";
        for(Jungle jungle: jungles.values()) {
            str += jungle.toString() + "\n";
        }
        str += "Total species: " + countTotalSpecies() + "\n\n";
        return str;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    
    public long countTotalSpecies() {
        long cs = 0;
        for(Jungle jungle: jungles.values()) {
            cs += jungle.countSpecies();
        }
        return cs;
    }
}

