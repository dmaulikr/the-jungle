package br.thejungle.environment.things.info;

import java.util.logging.Logger;

import br.thejungle.environment.things.Food;

/**
 * This is a wrapper class for exposing only desired properties of type Food
 */
public abstract class FoodInfo extends ThingInfo {

	protected short richness;
	protected short size;

	private static Logger logger = Logger.getLogger("br.thejungle.environment.things.FoodInfo");

	/**
	 * @param thing
	 */
	public FoodInfo(Food food) {
		super(food);
		richness = food.getRichness();
		size = food.getSize();
	}

	public short getRichness() {
		return richness;
	}

	public short getSize() {
		return size;
	}

	public String toString() {
		return "FoodInfo: " + super.toString() + "; richness="+richness + "; size="+size;
	}
	
}
