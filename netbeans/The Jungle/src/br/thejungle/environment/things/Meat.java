package br.thejungle.environment.things;

import br.thejungle.environment.things.info.MeatInfo;
import br.thejungle.species.Species;

/**
 * This implementation represents a meat food. It is associated to a dead
 * species
 */
public class Meat extends Food {

    private Species species;
    private MeatInfo meatInfo;
    private float contamination;
    
    public float CONTAMINATION_RATE = 0.03F;

    public Meat(short richness, short size, Species species) {
        super(richness, size);
        this.species = species;
        this.meatInfo = new MeatInfo(this);
        this.contamination = 0;
    }
    
    public Species getSpecies() {
        return species;
    }
    
    public MeatInfo getMeatInfo() {
        return meatInfo;
    }
    
    public void timeElapsed() {
        if(contamination<1) contamination += CONTAMINATION_RATE;
        if(contamination>1) contamination = 1;
        setRichness((short)(getRichness() - ((getRichness()*contamination))/3));
    }
    
    public float getContamination() {
        return contamination;
    }
    
    public boolean isValid() {
        return (getRichness()>0);
    }

}
