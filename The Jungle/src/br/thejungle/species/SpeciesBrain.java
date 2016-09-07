package br.thejungle.species;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import br.thejungle.environment.Task;
import br.thejungle.exceptions.BrainNotBoundToSpeciesException;
import br.thejungle.species.abilities.Ability;
import br.thejungle.species.abilities.Eat;
import br.thejungle.species.abilities.TaskControl;
import br.thejungle.species.abilities.Walk;
import br.thejungle.species.abilities.reproduction.Reproduce;
import br.thejungle.species.senses.Sense;

/**
 * This is the species's brain. The developer will program the Species AI here.
 */
public abstract class SpeciesBrain implements Serializable {

	/**
	 * This is the optimum value of food/size (nutrition)
	 */
	public static final float OPTIMUM_NUTRITION = 1;

	private Species species = null;

	/**
	 * This method is invoked while the species is alive for performing thinking
	 * operations. The thread that calls this method is individual by Species
	 * class, so you have to return this method at each step, or else the other
	 * individuals won't perform any thinking!
	 */
	public abstract void think();

	/**
	 * This method will be invoked when any sense is activated. For example, if
	 * the species sees anything it will fire this method.
	 */
	public abstract void senseFired(SenseEvent event);

	/**
	 * This method is invoked when any task that was submitted is finished. For
	 * example, this method is invoked when the species arrives its destination
	 * (walk ability)
	 */
	public abstract void taskPerformed(Task task);

	/**
	 * Gets the name of the species. Remember this is the name of the entire
	 * species, for individual ID, get the individualID property.
	 */
	public abstract String getSpeciesName();

	/**
	 * Gets the number of custom genes this species uses. These genes are
	 * automatically combined during reproduction and are accessible by invoking
	 * the getCustomGeneticCode() method.
	 */
	public abstract short getNumberOfCustomGenes();

	/**
	 * Returns the unique identificator of the species
	 */
	public String getIndividualID() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getIndividualID();
	}

	/**
	 * Returns the species's custom geneticCode. The user may use the elements
	 * of this array as genes. They vary from 0 to 1000
	 */
	public float[] getCustomGenecticCode() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getGenetics().getCustomGeneticCode();
	}

	/**
	 * Returns the current heath of the species
	 */
	public float getHealth() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getHealth();
	}

	/**
	 * Returns the current qtty of food in species
	 */
	public float getFood() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getFood();
	}

	/**
	 * Returns the age of the species
	 */
	public int getAge() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getAge();
	}

	/**
	 * Returns the size of the species
	 */
	public float getSize() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getSize();
	}

	/**
	 * Returns the current nutrition. This value must maintain the average value
	 * of OPTIMUM_NUTRITION
	 */
	public float getNutrition() {
		return getFood() / getSize();
	}

	public final void setSpecies(Species species) {
		this.species = species;
	}

	public CopyOnWriteArrayList<Ability> getAbilities() {
		return species.getAbilities();
	}

	public CopyOnWriteArrayList<Sense> getSenses() {
		return species.getSenses();
	}

	public String toString() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.toString();
	}

	public double getXPos() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getXPos();
	}

	public double getYPos() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getYPos();
	}

	// Abilities
	public Eat getAbilityEat() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getAbilityEat();
	}

	public Walk getAbilityWalk() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getAbilityWalk();
	}

	public Reproduce getAbilityReproduce() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getAbilityReproduce();
	}
	
	public TaskControl getAbilityTaskControl() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
		return species.getAbilityTaskControl();
	}
	
	public boolean isValid() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
	    return species.isValid();
	}
	
	public boolean isMale() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
	    return species.isMale();
	}
	
	public boolean isFemale() {
		if (species == null) throw new BrainNotBoundToSpeciesException();
	    return species.isFemale();
	}
	
	public SpeciesInfo getSpeciesInfo() {
		return species.getSpeciesInfo();
	}
	
}
