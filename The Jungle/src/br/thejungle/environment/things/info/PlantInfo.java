/*
 * PlantInfo.java
 *
 * Created on 4 de Dezembro de 2004, 07:56
 */

package br.thejungle.environment.things.info;

import br.thejungle.environment.things.Plant;

/**
 * 
 * @author Flávio
 */
public class PlantInfo extends FoodInfo {

	public PlantInfo(Plant plant) {
		super(plant);
	}

	public String toString() {
		return "PlantInfo: " + super.toString();
	}
}
