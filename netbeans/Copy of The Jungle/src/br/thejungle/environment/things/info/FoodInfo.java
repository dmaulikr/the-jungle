package br.thejungle.environment.things.info;

import br.thejungle.environment.things.Food;
import java.util.logging.Logger;


/**
 * This is a wrapper class for exposing only desired properties of type Food
 */
public abstract class FoodInfo extends ThingInfo {

    private static Logger logger = Logger.getLogger("br.thejungle.environment.things.FoodInfo");
    private Food food;

    /**
     * @param thing
     */
    public FoodInfo(Food food) {
        super(food);
        this.food = food;
    }

    public short getRichness() {
        return food.getRichness();
    }

    public short getSize() {
        return food.getSize();
    }

}
