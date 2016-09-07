package br.thejungle.species.abilities;

import br.thejungle.environment.things.Food;
import br.thejungle.environment.things.Meat;
import br.thejungle.environment.things.Plant;
import br.thejungle.environment.things.Thing;
import br.thejungle.environment.things.info.FoodInfo;
import br.thejungle.species.Species;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

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
    public void perform(short qtty) {
        CopyOnWriteArrayList<Thing> things = species.getJungle().getThings(species.getXPos(), species.getYPos(), species.getSize());
        short qttyEaten=0, qttyToEat = 0;
        for(Thing thing: things) {
            if(thing instanceof Food) {
                Food food = (Food)thing;
                qttyToEat = (short)Math.min((qtty-qttyEaten), food.getRichness());
                food.setRichness((short)(food.getRichness()-qttyToEat));
                species.addFood(getRichnessForSpecies(food, qttyToEat));
                qttyEaten += qttyToEat;
                if(qttyEaten>=qtty) {
                    break;
                }
            }
        }
    }
    
    
    private float getRichnessForSpecies(Food food, short qtty) {
            float eat = species.getGenetics().getAbilityEat();
            if(food instanceof Plant) {
                return (1-eat)*qtty;
            } else if (food instanceof Meat) {
                return eat*qtty;
            }
            logger.warning("The food " + food.getClass().getName() + " was neither a Plant nor a Meat");
            return 0;
    }
    
    /**
     * Goes to the food position and then tries to eat. This method is
     * assyncronous.
     */
    public void perform(FoodInfo food, short qtty) {
        //assynchronous
        species.getWalkAbility().gotoPos(food.getXPos(), food.getYPos());
        //assynchronous
        EatTask et = new EatTask(species, qtty);
        species.getJungle().executeTask(et);
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#perform()
     */
    public void perform() {
        perform(DEFAULT_QTTY);
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.abilities.Ability#isEffective()
     */
    public boolean isEffective() {
        return true;
   }

}
