/*
 * MeatInfo.java
 *
 * Created on 4 de Dezembro de 2004, 07:51
 */

package br.thejungle.environment.things.info;

import br.thejungle.species.SpeciesInfo;
import br.thejungle.environment.things.Meat;

/**
 *
 * @author  Flávio
 */
public class MeatInfo extends FoodInfo {
    
    private Meat meat;
    
    /** Creates a new instance of MeatInfo */
    public MeatInfo(Meat meat) {
        super(meat);
        this.meat = meat;
    }
    
    public SpeciesInfo getSpeciesInfo() {
        return meat.getSpecies().getSpeciesInfo();
    }
    
    public float getContamination() {
        return meat.getContamination();
    }
    
}
