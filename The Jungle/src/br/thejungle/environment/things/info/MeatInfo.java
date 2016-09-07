/*
 * MeatInfo.java
 *
 * Created on 4 de Dezembro de 2004, 07:51
 */

package br.thejungle.environment.things.info;

import br.thejungle.environment.things.Meat;
import br.thejungle.species.SpeciesInfo;

/**
 * 
 * @author Flávio
 */
public class MeatInfo extends FoodInfo {

	protected SpeciesInfo speciesInfo;
	protected float contamination;
	
	/** Creates a new instance of MeatInfo */
	public MeatInfo(Meat meat) {
		super(meat);
		speciesInfo = meat.getSpecies().getSpeciesInfo();
		contamination = meat.getContamination();
	}

	public SpeciesInfo getSpeciesInfo() {
		return speciesInfo;
	}

	public float getContamination() {
		return contamination;
	}

	public String toString() {
		return "MeatInfo: " + super.toString() + "; speciesInfo=" + speciesInfo + "; contamination="+contamination;
	}
}
