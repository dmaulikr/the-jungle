package br.thejungle.species;

import java.io.Serializable;
import java.util.Random;

/**
 * This class represents the genectic code of the species
 */
public class Genetics implements Serializable {

    static Random random = new Random();

    public static final short GENDER_MALE = 0;
    public static final short GENDER_FEMALE = 1;
    
    public static final short DEFAULT_MAX_VALUE = 200;

    private static final short NUMBER_OF_DEFAULT_GENES = 24;
    
    private static final short GENDER = 0;
    private static final short SIZE = 1;
    private static final short STRENGTH = 2;
    private static final short IQ = 3;
    private static final short FOOD_TENDENCY = 4;
    private static final short LIFE_TIME = 5;
    private static final short METABOLISM = 6;
    private static final short PARENT_KNOWLEDGE = 7;
    private static final short ABILITY_EAT = 8;
    private static final short ABILITY_REPRODUCE = 9;
    private static final short ABILITY_ATTACK = 10;
    private static final short ABILITY_WALK = 11;
    private static final short ABILITY_COOK = 12;
    private static final short ABILITY_FLY = 13;
    private static final short ABILITY_CURE = 14;
    private static final short ABILITY_TRAVEL_TO_OTHER_JUNGLE = 15;
    private static final short ABILITY_COMMUNICATE_TO = 16;
    private static final short SENSE_SMELL = 17;
    private static final short SENSE_SIGHT = 18;
    private static final short SENSE_NIGHT_VISION = 19;
    private static final short SENSE_HEARING = 20;
    private static final short SENSE_TOUCH = 21;
    private static final short SENSE_TASTE = 22;
    private static final short SENSE_UNDERSTAND_COMMUNICATION = 23;
    
    private float[] defaultGeneticCode;
    private float[] customGeneticCode;

    public Genetics combine(Genetics genetics) {
       return new Genetics(this, genetics);
    }
    
    private Genetics(Genetics genetics1, Genetics genetics2) {
    }

    public Genetics(short nCustomGenes) {
        //randomize the default genetic codes
        defaultGeneticCode = new float[NUMBER_OF_DEFAULT_GENES];
        for(int i=0; i<defaultGeneticCode.length;i++) {
            defaultGeneticCode[i] = random.nextFloat();
        }
        
        //randomize the custom genetic codes
        customGeneticCode = new float[nCustomGenes];
        for(int i=0; i<customGeneticCode.length;i++) {
            customGeneticCode[i] = random.nextFloat();
        }
    }
  

    /**
     * @return Returns the size.
     */
    public float getSize() {
        return defaultGeneticCode[SIZE];
    }
    /**
     * @return Returns the strength.
     */
    public float getStrength() {
        return defaultGeneticCode[STRENGTH];
    }
    
    
    /**
     * @return Returns the foodTendency.
     */
    public float getFoodTendency() {
        return defaultGeneticCode[FOOD_TENDENCY];
    }
    /**
     * @return Returns the gender.
     */
    public float getGender() {
        return defaultGeneticCode[GENDER];
    }
    /**
     * @return Returns the iq.
     */
    public float getIq() {
        return defaultGeneticCode[IQ];
    }
    /**
     * @return Returns the lifeTime.
     */
    public float getLifeTime() {
        return defaultGeneticCode[LIFE_TIME];
    }
    /**
     * @return Returns the metabolism.
     */
    public float getMetabolism() {
        return defaultGeneticCode[METABOLISM];
    }
    /**
     * @return Returns the parentKnowledge.
     */
    public float getParentKnowledge() {
        return defaultGeneticCode[PARENT_KNOWLEDGE];
    }

    
    /**
     * @return Returns the abilityAttack.
     */
    public float getAbilityAttack() {
        return defaultGeneticCode[ABILITY_ATTACK];
    }
    /**
     * @return Returns the abilityComunicateTo.
     */
    public float getAbilityCommunicateTo() {
        return defaultGeneticCode[ABILITY_COMMUNICATE_TO];
    }
    /**
     * @return Returns the abilityCook.
     */
    public float getAbilityCook() {
        return defaultGeneticCode[ABILITY_COOK];
    }
    /**
     * @return Returns the abilityCure.
     */
    public float getAbilityCure() {
        return defaultGeneticCode[ABILITY_CURE];
    }
    /**
     * @return Returns the abilityEat.
     */
    public float getAbilityEat() {
        return defaultGeneticCode[ABILITY_EAT];
    }
    /**
     * @return Returns the abilityFly.
     */
    public float getAbilityFly() {
        return defaultGeneticCode[ABILITY_FLY];
    }
    /**
     * @return Returns the abilityReproduce.
     */
    public float getAbilityReproduce() {
        return defaultGeneticCode[ABILITY_REPRODUCE];
    }
    /**
     * @return Returns the abilityTravelToOtherJungle.
     */
    public float getAbilityTravelToOtherJungle() {
        return defaultGeneticCode[ABILITY_TRAVEL_TO_OTHER_JUNGLE];
    }
    /**
     * @return Returns the abilityWalk.
     */
    public float getAbilityWalk() {
        return defaultGeneticCode[ABILITY_WALK];
    }
    /**
     * @return Returns the senseHearing.
     */
    public float getSenseHearing() {
        return defaultGeneticCode[SENSE_HEARING];
    }
    /**
     * @return Returns the senseNightVision.
     */
    public float getSenseNightVision() {
        return defaultGeneticCode[SENSE_NIGHT_VISION];
    }
    /**
     * @return Returns the senseSight.
     */
    public float getSenseSight() {
        return defaultGeneticCode[SENSE_SIGHT];
    }
    /**
     * @return Returns the senseSmell.
     */
    public float getSenseSmell() {
        return defaultGeneticCode[SENSE_SMELL];
    }
    /**
     * @return Returns the senseTaste.
     */
    public float getSenseTaste() {
        return defaultGeneticCode[SENSE_TASTE];
    }
    /**
     * @return Returns the senseTouch.
     */
    public float getSenseTouch() {
        return defaultGeneticCode[SENSE_TOUCH];
    }
    /**
     * @return Returns the senseUnderstandCommunication.
     */
    public float getSenseUnderstandCommunication() {
        return defaultGeneticCode[SENSE_UNDERSTAND_COMMUNICATION];
    }

    /**
     * @return Returns the customGeneticCode.
     */
    public float[] getCustomGeneticCode() {
        return customGeneticCode;
    }
    
    public String toString() {
        String str = "defaultGeneticCode=";
        for(float f: defaultGeneticCode) str += f + ",";
        str = str.substring(0, str.length()-1);
        str += "; ncustomGeneticCode=";
        for(float f: customGeneticCode) str += f + ",";
        str = str.substring(0, str.length()-1);
        return str;
    }
    
}
