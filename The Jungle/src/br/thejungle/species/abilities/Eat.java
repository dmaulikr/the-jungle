package br.thejungle.species.abilities;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import br.thejungle.environment.things.Food;
import br.thejungle.environment.things.Meat;
import br.thejungle.environment.things.Plant;
import br.thejungle.environment.things.Thing;
import br.thejungle.species.Species;

/**
 * Here is implemented the ability of eating
 */
public class Eat extends Ability {

	private static Logger logger = Logger.getLogger("br.thejungle.abilities.Eat");

	public static final short DEFAULT_QTTY = 30;

	/**
	 * @param species
	 */
	public Eat(Species species) {
		super(species);
	}

	/**
	 * Tries to eat whatever is on current position. This method is syncronous.
	 */
	public boolean perform(short qtty) {
		CopyOnWriteArrayList<Thing> things = species.getJungle().getThings(
				species.getXPos(), species.getYPos(), species.getSize());
		short qttyEaten = 0, qttyToEat = 0;
		for (Thing thing : things) {
			if (thing instanceof Food) {
				Food food = (Food) thing;
				qttyToEat = (short) Math.min((qtty - qttyEaten), food
						.getRichness());
				food.setRichness((short) (food.getRichness() - qttyToEat));
				species.addFood(getRichnessForSpecies(food, qttyToEat));
				qttyEaten += qttyToEat;
				if (qttyEaten >= qtty) {
					break;
				}
				return true;
			}
		}
		return false;
	}

	private float getRichnessForSpecies(Food food, short qtty) {
		float eat = species.getGenetics().getAbilityEat();
		if (food instanceof Plant) {
			return (1 - eat) * qtty;
		} else if (food instanceof Meat) {
			return eat * qtty;
		}
		logger.warning("The food " + food.getClass().getName()
				+ " was neither a Plant nor a Meat");
		return 0;
	}

	/**
	 * This method is
	 * assyncronous.
	 */
	public void schedulePerform(short qtty) {
		// assynchronous
		EatTask et = new EatTask(species, qtty);
		species.getJungle().executeTask(species.getSpeciesBrain(), et);
	}

	public void schedulePerform() {
		// assynchronous
	    schedulePerform(DEFAULT_QTTY);
	}

	/**
	 * Goes to the food position and then tries to eat. This method is
	 * assyncronous.
	 */
/*	public void perform(FoodInfo food) {
		perform(food, DEFAULT_QTTY);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#perform()
	 */
	public boolean perform() {
		return perform(DEFAULT_QTTY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#isEffective()
	 */
	public boolean isEffective() {
		return true;
	}

}
