package br.thejungle.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.thejungle.environment.things.Thing;
import br.thejungle.exceptions.BrainNotInstantiableException;
import br.thejungle.exceptions.DuplicateJungleException;
import br.thejungle.exceptions.JungleNotFoundException;
import br.thejungle.species.Species;

/**
 * This is the jungle's portal to the World. The Nature represents all jungles
 * states, so it must be able to persist itself at shutdown and recover the last
 * state on startup.
 */
public class Nature implements Serializable, INature {

    private transient static Nature instance = null;

    private ConcurrentMap<String, Jungle> jungles;
    private int timesShutDown;
    private Date creationDate;
    private boolean active;

	transient private static Random random = new Random();
    transient private static Logger logger = Logger.getLogger("br.thejungle.environment.Nature");

    transient private static final String SAVE_FILE_NAME = "Nature.ser";

    public static final int THREAD_SLEEP_TIME_PER_CICLE = 50;
    public static final int THREAD_TERMINATION_TIMEOUT = 60000;
    public static final int THREAD_KEEP_ALIVE_TIME = 15000;
    public static final String RMI_NAME = "thejungle/Nature";
    public static final String RMI_CODEBASE = "http://localhost:9090/development/The%20Jungle/The%20Jungle/bin/";
    
    /**
     * Singleton pattern implementation
     */
    private Nature() {
        logger.fine("Creating new Nature");
        this.jungles = new ConcurrentHashMap<String, Jungle>();
        this.creationDate = Calendar.getInstance().getTime();
    }

    public static void main(String[] args) throws RemoteException,
            MalformedURLException {
        Logger.getLogger("").setLevel(Level.INFO);
        for (Handler h : Logger.getLogger("").getHandlers()) {
            h.setLevel(Level.ALL);
        }

        logger.info("Registering *The Jungle* server as \"" + RMI_NAME
                + "\" in rmiregistry...");

        Nature nature = Nature.getInstance();

        System.setProperty("java.rmi.server.codebase", RMI_CODEBASE);

        Naming.rebind(RMI_NAME, nature);
        logger.info("*The Jungle* server ready and running");
    }

    public static Nature getInstance() {
        logger.fine("Getting nature instance");
        if (instance == null) {
            try {
                instance = restoreLastState();
            } catch (Exception e) {
                logger
                        .warning("Error on deserialization of last Nature state. "
                                + e.getMessage());
            }
        }
        if (instance == null) {
            instance = new Nature();
            logger.info("A new Nature instance was created");

        } else {
            logger.info("An existing instance of Nature is being used");

        }
        return instance;
    }

    public void createJungle(String jungleName, long width, long height,
            float mutationRate) throws DuplicateJungleException {
        if (jungles.get(jungleName) != null)
            throw new DuplicateJungleException("Jungle named " + jungleName
                    + " already exists");
        Jungle jungle = new Jungle(jungleName, width, height, mutationRate);
        jungles.put(jungleName, jungle);
        if(active) jungle.startup();
        logger.fine("Jungle " + jungleName + " created");
    }

    public void removeJungle(String jungleName) throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungles.remove(jungleName);
        if(active) jungle.shutdown();
        logger.fine("Jungle " + jungleName + " removed");
    }

    public void createSpecies(String jungleName, Developer developer,Class brainClass) throws JungleNotFoundException,
            BrainNotInstantiableException {
        try {

            Jungle jungle = getJungle(jungleName);
            Species species = new Species(developer, brainClass);
            jungle.acceptSpecies(species);

        } catch (InstantiationException e) {
            logger.throwing("Nature","createSpecies(String jungleName, Developer developer,Class brainClass)",e);
            throw new BrainNotInstantiableException("The SpeciesBrain "
                    + brainClass.getName()
                    + " couldn't be instantiated", e);
        } catch (IllegalAccessException e) {
            logger.throwing("Nature","createSpecies(String jungleName, Developer developer,Class brainClass)",e);
            throw new BrainNotInstantiableException("The SpeciesBrain "
                    + brainClass.getName()
                    + " couldn't be instantiated", e);
        }
    }

    public void acceptSpecies(String jungleName, Species species)
            throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.acceptSpecies(species);
    }

    public void removeSpecies(String jungleName, String speciesName)
            throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.removeSpecies(speciesName);
    }

    public void removeSpecies(String speciesName) {
        for (Jungle j : jungles.values()) {
            j.removeSpecies(speciesName);
        }
    }

    public void removeAllSpecies(String jungleName)
            throws JungleNotFoundException {
        Jungle jungle = getJungle(jungleName);
        jungle.removeAllSpecies();
    }

    private Jungle getJungle(String jungleName) throws JungleNotFoundException {
        Jungle jungle = jungles.get(jungleName);
        if (jungle == null)
            throw new JungleNotFoundException("Jungle named " + jungleName
                    + " not found in this Nature");
        return jungle;
    }

    /**
     * Performs the persistence of self state. Persisting the self state will
     * make all jungles and agregates alive on disk! This class is always a
     * singleton from a previous saved state.
     * 
     * @throws IOException
     */
    public void saveCurrentState() throws IOException {
        File file = new File(SAVE_FILE_NAME);
        if (file.exists())
            file.delete();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        logger.fine("Current Nature state saved on disk");
    }

    public static Nature restoreLastState() throws IOException,
            ClassNotFoundException {
        logger.fine("Restoring last state");
        FileInputStream fis = new FileInputStream(SAVE_FILE_NAME);
        ObjectInputStream ois = new ObjectInputStream(fis);

        if (instance != null)
            instance.shutdown();
        Nature ninstance = (Nature) ois.readObject();

        ois.close();

        return ninstance;
    }

    /**
     * Mark the jungles as active and create threads for them
     */
    public void startup() {
    	// do nothing if Nature is already active
    	if(active) return;
    	
    	active = true;
    	
    	// startup all jungles
        for (Jungle jungle : jungles.values()) {
        	jungle.startup();
        }

    }

    /**
     * Mark the jungles as inactive
     */
    public void shutdown() {
    	// do nothing if Nature is already inactive
    	if(!active) return;
    	
    	// shutdown all jungles
        for (Jungle j : jungles.values()) {
            j.shutdown();
        }
        timesShutDown++;
    	active = false;
    	
        logger.info("Nature was shutdown. Shutdown times: " + timesShutDown);
    }

    /**
     * Calls the saveCurrentState() method to save the current state before
     * being garbage collected.
     * 
     * @throws IOException
     */
    /*
     * public void finalize() throws IOException { this.shutdown();
     * this.saveCurrentState(); }
     */

    public Map<String, Jungle> getJungles() {
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
        for (Jungle jungle : jungles.values()) {
            str += jungle.toString() + "\n";
        }
        return str;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long countTotalSpecies() {
        long cs = 0;
        for (Jungle jungle : jungles.values()) {
            cs += jungle.countSpecies();
        }
        return cs;
    }

    public long countTotalMeats() {
        long cs = 0;
        for (Jungle jungle : jungles.values()) {
            cs += jungle.countMeats();
        }
        return cs;
    }

    public long countTotalPlants() {
        long cs = 0;
        for (Jungle jungle : jungles.values()) {
            cs += jungle.countPlants();
        }
        return cs;
    }

	public Collection<Thing> getAllThings(String jungleName) throws RemoteException, JungleNotFoundException {
		Jungle jungle = jungles.get(jungleName);
		if(jungle==null) throw new JungleNotFoundException("Jungle named " + jungleName + " not found in Nature");
		return jungle.getAllThings();
	}
}
