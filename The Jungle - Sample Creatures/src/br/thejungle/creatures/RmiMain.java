/*
 * RmiMain.java
 *
 * Created on December 6, 2004, 6:20 PM
 */

package br.thejungle.creatures;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.thejungle.environment.INature;
import br.thejungle.environment.Nature;
import br.thejungle.exceptions.DuplicateJungleException;
import br.thejungle.exceptions.JungleNotFoundException;

/**
 * 
 * @author f3310193
 */
public class RmiMain {

	public static void main(String[] args) throws RemoteException, JungleNotFoundException, MalformedURLException, NotBoundException, DuplicateJungleException {
		Logger.getLogger("").setLevel(Level.INFO);
		for (Handler h : Logger.getLogger("").getHandlers()) {
			h.setLevel(Level.ALL);
		}
		
		INature nature = (INature) Naming.lookup("rmi://localhost/" + Nature.RMI_NAME);

		nature.createJungle("Test1", 100L, 100L, 0.01F);
		//nature.removeJungle("Test1");
		// nature.createSpecies("", new Developer(), SimpleGuyBrain.class);

		System.out.println(nature);
		
	}

}
