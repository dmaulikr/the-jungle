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
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * This is the jungle's portal to the World.
 * The Nature represents all jungles states, so it must be able to persist itself at shutdown and recover the last state on startup.
 */
public class Nature implements Serializable, INature {
    
    private transient static final String SAVE_FILE_NAME = "Nature.ser";
    private transient static Nature instance = null;
    private ConcurrentMap<String, Jungle> jungles;
    private int timesShutDown;
    private Date creationDate;
    private static Logger logger = Logger.getLogger("br.thejungle.environment.Nature");

    transient static private ThreadPoolExecutor threadPool;
    public static final int MAX_JUNGLES = 15;
    
    public static final int THREAD_TERMINATION_TIMEOUT = 60000;
    public static final int THREAD_KEEP_ALIVE_TIME = 15000;
    
    public static final String RMI_NAME = "thejungle/Nature";
    
    /**
     * Singleton pattern implementation
     */
    private Nature() {
        logger.fine("Creating new Nature");
        this.jungles = new ConcurrentHashMap<String, Jungle>();
        this.creationDate = Calendar.getInstance().getTime();
    }
    
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        logger.setLevel(Level.ALL);
        for(Handler h: logger.getHandlers()) {
            h.setLevel(Level.ALL);
        }
        logger.info("Registering *The Jungle* server as \"" + RMI_NAME + "\" in rmiregistry...");
        Nature nature = Nature.getInstance();
//        System.setProperty("java.rmi.server.codebase","file:\\development\\The Jungle\\netbeans\\The Jungle\\build\\classes/");
        Naming.rebind(RMI_NAME, nature);
        logger.info("*The Jungle* server ready and running");
    }
    
    public static Nature getInstance() {
        logger.fine("Getting nature instance");
        if(instance==null) {
            try {
                threadPool = new ThreadPoolExecutor(1, MAX_JUNGLES, THREAD_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(MAX_JUNGLES));
                instance = restoreLastState();
            } catch (Exception e) {
                logger.warning("Error on deserialization of last Nature state. " + e.getMessage());
            }
        }
        if(instance==null) instance = new Nature();
        return instance;
    }
    
    public void createJungle(String jungleName, long width, long height, float mutationRate) throws DuplicateJungleException {
        if(jungles.get(jungleName)!=null) throw new DuplicateJungleException("Jungle named " + jungleName + " already exists");
        Jungle jungle = new Jungle(jungleName, width, height, mutationRate);
        jungles.put(jungleName, jungle);
        jungle.setActive(true);
        threadPool.execute(jungle);
        logger.info("Jungle " + jungleName + " created");
    }
    
    public void removeJungle(String jungleName) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungles.remove(jungleName);
        jungle.setActive(false);
        logger.info("Jungle " + jungleName + " removed");
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
        logger.fine("Restoring last state");
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
    private void startup() {
        for(Jungle jungle: jungles.values()) {
            jungle.setActive(true);
            threadPool.execute(jungle);
            logger.info("Jungle " + jungle.getJungleName() + " started");
        }
    }
    
    /**
     * Mark the jungles as inactives
     */
    public void shutdown() {
        //set as inactive so that it won't perform new tasks
        for(Jungle j: jungles.values()) {
            j.setActive(false);
        }
        //wait for the jungles thread to finish all current tasks
        try {
            threadPool.awaitTermination(THREAD_TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.severe("Couldn't wait for the jungles to finish their tasks: " + e.getMessage());
        }
        timesShutDown++;
    }
    
    /**
     *Calls the saveCurrentState() method to save the current state before being garbage collected.
     * @throws IOException
     */
    /*public void finalize() throws IOException {
        this.shutdown();
        this.saveCurrentState();
    }*/
    
    public Map getJungles() {
        return jungles;
    }
    
    public String toString() {
        int cs = 0;
        String str = "Nature alive since " + creationDate + "\n\n";
        str += "Shutdown times: " + timesShutDown + "\n";
        str += "Total species: " + countTotalSpecies() + "\n";
        str += "Total plants: " + countTotalPlants() + "\n";
        str += "Total meats: " + countTotalMeats() + "\n";
        str += "Jungles: " + jungles.values().size() + "\n\n";
        for(Jungle jungle: jungles.values()) {
            str += jungle.toString() + "\n";
        }
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

    public long countTotalMeats() {
        long cs = 0;
        for(Jungle jungle: jungles.values()) {
            cs += jungle.countMeats();
        }
        return cs;
    }

    public long countTotalPlants() {
        long cs = 0;
        for(Jungle jungle: jungles.values()) {
            cs += jungle.countPlants();
        }
        return cs;
    }
}

