package br.thejungle.species;

import java.util.Random;

import br.thejungle.environment.Developer;
import br.thejungle.environment.Jungle;
import br.thejungle.environment.things.Thing;
import br.thejungle.species.abilities.Ability;
import br.thejungle.species.abilities.Eat;
import br.thejungle.species.abilities.Reproduce;
import br.thejungle.species.abilities.Walk;
import br.thejungle.species.senses.Sense;
import br.thejungle.species.senses.Sight;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * This is the Species main body containing all properties and operations
 * concerning to the species
 */
public class Species extends Thing {

    private String individualID;
    private float age;
    private float health;
    private float food;
    private Jungle jungle;
    private SpeciesInfo speciesInfo;
    private CopyOnWriteArrayList<Sense> senses;
    private Developer developer;
    private CopyOnWriteArrayList<Ability> abilities;
    private SpeciesBrain speciesBrain;
    private Genetics genetics;
    private static Random random = new Random();
    private static Logger logger = Logger.getLogger("br.thejungle.species.Species");
    
    public static final short MAX_SIZE = 200;
    public static final short MAX_STRENGTH = 200;
    public static final long MAX_LIFE_TIME = 1000;
    public static final short MAX_HEALTH = 200;
    public static final float MAX_METABOLISM = 0.5F;//relation of (food/size)/time (quantity of food per size per time unit)
    
    /**
     * Relation between nutrition and health. Impact for being too fat or starving
     * The too fat or starving, the health will be affected by |factor*(optimumFood-realFood)|
     */
    public static final float NUTRITION_HEALTH_FACTOR = 0.001F;
    public static final float VAR_NUTRITION = 0.3F;//variation in nutrition value in relation to OPTIMUM_NUTRITION without injuries
    

    /**
     * This constructor should be invoked when creating a new species during reproduction
     * @param developer
     * @param brain
     * @param genetics
     */
    public Species(Developer developer, SpeciesBrain brain, Genetics genetics) {
        this.speciesBrain = brain;
        this.developer = developer;
        this.genetics = genetics;
        individualID = brain.getSpeciesName() + "-" + random.nextLong();
        age = 0;
        health = MAX_HEALTH;
        food = MAX_SIZE * (SpeciesBrain.OPTIMUM_NUTRITION+VAR_NUTRITION);//mas food allowed without impact in health
        jungle = null;
        speciesInfo = new SpeciesInfo(this);
        abilities = new CopyOnWriteArrayList<Ability>();
        senses = new CopyOnWriteArrayList<Sense>();
        computeSensesAbilities(genetics);
    }
    
    /**
     * This constructor should be invoked when creating the very first species of a kind. Its genetics will be randomized
     * @param developer
     * @param brain
     */
    public Species(Developer developer, SpeciesBrain brain) {
        this(developer, brain, new Genetics(brain.getNumberOfCustomGenes()));
    }
    
    private void computeSensesAbilities(Genetics genetics) {
        
        //Abilities
        Eat eat = new Eat(this);
        if(eat.isEffective()) abilities.add(eat);

        Reproduce reproduce = new Reproduce(this);
        if(reproduce.isEffective()) abilities.add(reproduce);

        Walk walk = new Walk(this);
        if(eat.isEffective()) abilities.add(walk);

        //Senses
        Sight sight = new Sight(this);
        if(sight.isEffective()) senses.add(sight);
        
    }

    /**
     * This method is invoked by the jungle to inform the species that time has passed
     */
    public void timeElapsed() {
        age++;
        
        //consume food just for being alive
        consumeFood(getMetabolism()*getSize());
        
        //verify if starving or too fat
        int size = getSize();
        if((food<(size*(SpeciesBrain.OPTIMUM_NUTRITION+VAR_NUTRITION))) || (food<(size*(SpeciesBrain.OPTIMUM_NUTRITION-VAR_NUTRITION)))) {
            float optimumFood = size * SpeciesBrain.OPTIMUM_NUTRITION;
            health -= Math.abs((optimumFood-food)*(NUTRITION_HEALTH_FACTOR));
        }
        
        //loose health for being old until death
        float m = age/getLifeTime();
        if(m>=(0.75F)) {
            health -= (MAX_HEALTH-health)*getProportionalRate(age, getLifeTime(), 0.75F);
        }
       
        logger.fine(getIndividualID() + ": age=" + age + "; size="+size + "; health="+health + "; strength="+getStrength() + "; food="+ food + "; optfood="+ (size*SpeciesBrain.OPTIMUM_NUTRITION) + "; multiplierAscDesc()=" + getAgingMultiplierAscDesc() + "; multiplierAsc()="+getAgingMultiplierAsc());
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
        return (int)age;
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
     * @param jungle The jungle to set.
     */
    public void setJungle(Jungle jungle) {
        this.jungle = jungle;
    }
    /**
     * @return Returns the speciesInfo.
     */
    public SpeciesInfo getSpeciesInfo() {
        return speciesInfo;
    }

    public short getSize() {
        return (short)(Math.round(genetics.getSize()*MAX_SIZE*getAgingMultiplierAsc()));
    }
    
    public short getStrength() {
        return (short)Math.round(genetics.getStrength()*MAX_STRENGTH*getAgingMultiplierAscDesc()*(health/MAX_HEALTH));
    }
    
    public float getMetabolism() {
        return genetics.getMetabolism()*MAX_METABOLISM;
    }

    public long getLifeTime() {
        return Math.round(genetics.getLifeTime()*MAX_LIFE_TIME)+1;
    }

    public float getAgingMultiplierAscDesc() {
        float m = age/getLifeTime();
        if(m<(0.75F)) {
            return getAgingMultiplierAsc();
            
        } else {
            //formulado em papel  
            return 1-(getProportionalRate(age, getLifeTime(), 0.75F));
        }
    }

    /**
     * Returns a relation between the interval (maxValue - percMaxValue*maxValue) and the currentValue
     * This method was created with help of a pen and a paper, so try to understand it doing so!
     */
    private float getProportionalRate(float currentValue, float maxValue, float percMaxValue) {
            float da = currentValue - (percMaxValue*maxValue);
            float alt = maxValue - (percMaxValue*maxValue);
            return da/alt;
    }

    public float getAgingMultiplierAsc() {
        float m = age/getLifeTime();
        if(m<(0.25F)) {
            return m*4;
            
        } else {
            return 1;
            
        }
    }

    public void addFood(float food) {
        this.food += food;
    }
    public void consumeFood(float food) {
        this.food -= food;
        if(this.food<0) this.food = 0;
    }
    public void setHealth(short health) {
        this.health = health;
    }
    
    public String getSpeciesName() {
        return speciesBrain.getSpeciesName();
    }
    
    public String toString() {
        String str = "individualID="+individualID+"\n";
        str += "age="+age+"; ";
        str += "health="+health+"; ";
        str += "lifeTime="+getLifeTime()+"; ";
        str += "food="+food+"\n";
        str += "size="+getSize()+"; ";
        str += "strength="+getStrength()+"\n";
        str += "genetics="+genetics+"\n";
        str += "senses="+senses+"\n";
        str += "abilities="+abilities+"\n";
        return str;
    }
    
    public boolean isValid() {
        return (health>0);
    }
    
    public Eat getEatAbility() {
        for(Ability ability: abilities) {
            if(ability instanceof Eat) return (Eat)ability;
        }
        return null;
    }
    
    public Walk getWalkAbility() {
        for(Ability ability: abilities) {
            if(ability instanceof Walk) return (Walk)ability;
        }
        return null;
    }
    
    public Reproduce getReproduceAbility() {
        for(Ability ability: abilities) {
            if(ability instanceof Reproduce) return (Reproduce)ability;
        }
        return null;
    }
    
}
