/*
 * INature.java
 *
 * Created on December 6, 2004, 4:44 PM
 */

package br.thejungle.environment;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import br.thejungle.environment.things.Thing;
import br.thejungle.exceptions.BrainNotInstantiableException;
import br.thejungle.exceptions.DuplicateJungleException;
import br.thejungle.exceptions.JungleNotFoundException;
import br.thejungle.species.Species;

/**
 * 
 * @author f3310193
 */
public interface INature extends Remote {

	public void createJungle(String jungleName, long width, long height,
			float mutationRate) throws DuplicateJungleException,
			RemoteException;

	public void removeJungle(String jungleName) throws JungleNotFoundException,
			RemoteException;

	public void createSpecies(String jungleName, Developer developer,
			Class speciesBrain) throws JungleNotFoundException,
			BrainNotInstantiableException, RemoteException;

	public void acceptSpecies(String jungleName, Species species)
			throws JungleNotFoundException, RemoteException;

	public void removeSpecies(String jungleName, String speciesName)
			throws JungleNotFoundException, RemoteException;

	public void removeSpecies(String speciesName) throws RemoteException;

	public void removeAllSpecies(String jungleName)
			throws JungleNotFoundException, RemoteException;

	public void shutdown() throws RemoteException;

	public Map<String, Jungle> getJungles() throws RemoteException;

	public Date getCreationDate() throws RemoteException;

	public long countTotalSpecies() throws RemoteException;

	public long countTotalMeats() throws RemoteException;

	public long countTotalPlants() throws RemoteException;

	public void saveCurrentState() throws IOException, RemoteException;
	
	public Collection<Thing> getAllThings(String jungleName) throws RemoteException, JungleNotFoundException;
}
