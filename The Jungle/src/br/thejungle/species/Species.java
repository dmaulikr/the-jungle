package br.thejungle.species;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import br.thejungle.environment.Developer;
import br.thejungle.environment.Jungle;
import br.thejungle.environment.things.Thing;
import br.thejungle.environment.things.info.ThingInfo;
import br.thejungle.species.abilities.Ability;
import br.thejungle.species.abilities.Eat;
import br.thejungle.species.abilities.TaskControl;
import br.thejungle.species.abilities.Walk;
import br.thejungle.species.abilities.reproduction.Reproduce;
import br.thejungle.species.senses.Sense;
import br.thejungle.species.senses.Sight;
import br.thejungle.util.MathUtil;

/**
 * This is the Species main body containing all properties and operations
 * concerning to the species
 */
public class Species extends Thing {

	private String individualID;
	private int age;
	private float health;
	private float food;
	private Jungle jungle;
	private CopyOnWriteArrayList<Sense> senses;
	private Developer developer;
	private CopyOnWriteArrayList<Ability> abilities;
	private SpeciesBrain speciesBrain;
	private Genetics genetics;

	private static Random random = new Random();
	private static Logger logger = Logger.getLogger("br.thejungle.species.Species");
	
	public static final short MAX_SIZE = 20;
	public static final short MAX_STRENGTH = 200;
	public static final long MAX_LIFE_TIME = 1000;
	public static final short MAX_HEALTH = 200;
	public static final float MAX_METABOLISM = 0.5F;// relation of (food/size)/time (quantity of food per size per time unit)
	
	/**
	 * Relation between nutrition and health. Impact for being too fat or
	 * starving The too fat or starving, the health will be affected by
	 * |factor*(optimumFood-realFood)|
	 */
	public static final float NUTRITION_HEALTH_FACTOR = 0.001F;
	public static final float VAR_NUTRITION = 0.5F;// variation in nutrition value in relation to OPTIMUM_NUTRITION without injuries

	/**
	 * This constructor should be invoked when creating a new species during
	 * reproduction
	 * 
	 * @param developer
	 * @param brain
	 * @param genetics
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Species(Developer developer, Class brainClass, Genetics genetics) throws InstantiationException, IllegalAccessException {
        this.speciesBrain = (SpeciesBrain) brainClass.newInstance();
        this.speciesBrain.setSpecies(this);

        if(genetics==null) {
            genetics = new Genetics(speciesBrain.getNumberOfCustomGenes());
    		logger.finer("Brand new species " + speciesBrain.getSpeciesName() + " created");
            
        } else {
    		logger.finer("New species " + speciesBrain.getSpeciesName() + " created by reproduction");
        }
        
		this.individualID = speciesBrain.getSpeciesName() + "-" + random.nextLong();
		this.age = 0;
		this.health = MAX_HEALTH;
//		this.food = (genetics.getSize()*MAX_SIZE)*(SpeciesBrain.OPTIMUM_NUTRITION+VAR_NUTRITION);// max food allowed without impact in health
		this.food = 100;
		this.jungle = null;
		this.abilities = new CopyOnWriteArrayList<Ability>();
		this.developer = developer;
		this.genetics = genetics;
		this.senses = new CopyOnWriteArrayList<Sense>();
		computeSensesAbilities(genetics);
	}

	/**
	 * This constructor should be invoked when creating the very first species
	 * of a kind. Its genetics will be randomized
	 * 
	 * @param developer
	 * @param brain
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Species(Developer developer, Class brainClass) throws InstantiationException, IllegalAccessException {
		this(developer, brainClass, null);
	}

	private void computeSensesAbilities(Genetics genetics) {

		// Abilities
		abilities.add(new Eat(this));

		abilities.add(new Reproduce(this));

		abilities.add(new Walk(this));

		// Senses
		senses.add(new Sight(this));

	}

	/**
	 * This method is invoked by the jungle to inform the species that time has
	 * passed
	 */
	public void timeElapsed() {
		if(health<=0) return;
		
		age++;

		//if(age>=getLifeTime()) health = 0;
		
		// consume food just for being alive
		consumeFood((float)(getMetabolism() * getSize()));

		// verify if starving or too fat
		float size = getSize();
		if ((food < (size * (SpeciesBrain.OPTIMUM_NUTRITION + VAR_NUTRITION)))
				|| (food < (size * (SpeciesBrain.OPTIMUM_NUTRITION - VAR_NUTRITION)))) {
			float optimumFood = size * SpeciesBrain.OPTIMUM_NUTRITION;
			health -= Math.abs((optimumFood - food) * (NUTRITION_HEALTH_FACTOR));
		}

		// loose health for being old until death
		float m = (float)age / (float)getLifeTime();
		if (m >= (0.75F)) {
			health -= (MAX_HEALTH - health)
					* MathUtil.getProportionalRate(age, getLifeTime(), 0.75F);
		}

		logger.finest(getIndividualID() + ": age=" + age + "; size=" + size
				+ "; health=" + health + "; strength=" + getStrength()
				+ "; food=" + food + "; optfood="
				+ (size * SpeciesBrain.OPTIMUM_NUTRITION)
				+ "; multiplierAscDesc()=" + getAgingMultiplierAscDesc()
				+ "; multiplierAsc()=" + getAgingMultiplierAsc());
	}

	/**
	 * @return Returns the ability.
	 */
	public CopyOnWriteArrayList<Ability> getAbilities() {
		return abilities;
	}

	/**
	 * @return Returns the age.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return Returns the developer.
	 */
	public Developer getDeveloper() {
		return developer;
	}

	/**
	 * @return Returns the food.
	 */
	public float getFood() {
		return food;
	}

	/**
	 * @return Returns the genetics.
	 */
	public Genetics getGenetics() {
		return genetics;
	}

	/**
	 * @return Returns the health.
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * @return Returns the individualID.
	 */
	public String getIndividualID() {
		return individualID;
	}

	/**
	 * @return Returns the jungle.
	 */
	public Jungle getJungle() {
		return jungle;
	}

	/**
	 * @return Returns the sense.
	 */
	public CopyOnWriteArrayList<Sense> getSenses() {
		return senses;
	}

	/**
	 * @return Returns the speciesBrain.
	 */
	public SpeciesBrain getSpeciesBrain() {
		return speciesBrain;
	}

	/**
	 * @param jungle
	 *            The jungle to set.
	 */
	public void setJungle(Jungle jungle) {
		this.jungle = jungle;
	}

	/**
	 * @return Returns the speciesInfo.
	 */
	public SpeciesInfo getSpeciesInfo() {
		return new SpeciesInfo(this);
	}

	public ThingInfo getThingInfo() {
		return getSpeciesInfo();
	}

	public float getSize() {
		return (Math.round(genetics.getSize()*MAX_SIZE*getAgingMultiplierAsc()));
	}

	public double getStrength() {
		return Math.round(genetics.getStrength() * MAX_STRENGTH * getAgingMultiplierAscDesc() * (health / MAX_HEALTH));
	}

	public float getMetabolism() {
		return genetics.getMetabolism() * MAX_METABOLISM;
	}

	public long getLifeTime() {
		return Math.round(genetics.getLifeTime() * MAX_LIFE_TIME) + 1;
	}
	
	public float getAgingMultiplierAscDesc() {
		float m = (float)age / (float)getLifeTime();
		if (m < (0.75F)) {
			return getAgingMultiplierAsc();

		} else {
			// formulado em papel
			return 1 - (MathUtil.getProportionalRate(age, getLifeTime(), 0.75F));
		}
	}

	public float getAgingMultiplierAsc() {
		float m = (float)age / (float)getLifeTime();
		if (m < (0.25F)) {
			return m * 4;

		} else {
			return 1;

		}
	}

	public void addFood(float food) {
		this.food += food;
	}

	public void consumeFood(float food) {
		this.food -= food;
		if (this.food < 0) this.food = 0;
	}

	public void setHealth(short health) {
		this.health = health;
	}

	public String getSpeciesName() {
		return speciesBrain.getSpeciesName();
	}

	public boolean isValid() {
		return (health > 0);
	}

	public Eat getAbilityEat() {
		for (Ability ability : abilities) {
			if (ability instanceof Eat) return (Eat) ability;
		}
		return null;
	}

	public Walk getAbilityWalk() {
		for (Ability ability : abilities) {
			if (ability instanceof Walk) return (Walk) ability;
		}
		return null;
	}

	public Reproduce getAbilityReproduce() {
		for (Ability ability : abilities) {
			if (ability instanceof Reproduce) return (Reproduce) ability;
		}
		return null;
	}

	public TaskControl getAbilityTaskControl() {
		for (Ability ability : abilities) {
			if (ability instanceof TaskControl) return (TaskControl) ability;
		}
		return null;
	}

	public Sight getSightSense() {
		for (Sense sense : senses) {
			if (sense instanceof Sight) return (Sight) sense;
		}
		return null;
	}

	public short getMaxChildren() {
	    return (short)(getAbilityReproduce().getMaxChildren() * getAgingMultiplierAscDesc());
	}
	
	public boolean isMale() {
	    return getAbilityReproduce().isMale();
	}

	public boolean isFemale() {
	    return getAbilityReproduce().isFemale();
	}

	public String toString() {
		String str = "individualID=" + individualID + "; ";
		str += "age=" + age + "; ";
		str += "health=" + health + "; ";
		str += "lifeTime=" + getLifeTime() + "; ";
		str += "food=" + food + "; ";
		str += "size=" + getSize() + "; ";
		str += "strength=" + getStrength() + "; ";
		str += "genetics=" + genetics + "; ";
		str += "senses=" + senses + "; ";
		str += "abilities=" + abilities;
		return str;
	}

}
