/*
 * Created on 30/11/2004
 */
package br.thejungle.creatures.simpleGuy;

import java.util.logging.Logger;

import br.thejungle.environment.Task;
import br.thejungle.environment.things.info.PlantInfo;
import br.thejungle.species.SenseEvent;
import br.thejungle.species.SpeciesBrain;
import br.thejungle.species.SpeciesInfo;
import br.thejungle.species.abilities.EatTask;
import br.thejungle.species.abilities.WalkTask;
import br.thejungle.species.abilities.reproduction.ReproductionRequestEvent;
import br.thejungle.species.abilities.reproduction.RequestReproductionTask;
import br.thejungle.species.senses.SightEvent;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class SimpleGuyBrain extends SpeciesBrain {

	SpeciesInfo partnerInfo = null;

	private static Logger logger = Logger.getLogger("br.thejungle.creatures.simpleGuy.SimpleGuyBrain");

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.SpeciesBrain#think()
	 */
	public void think() {
/*		if (first) getWalkAbility().gotoPos(0, 0);
		first = false;*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.SpeciesBrain#senseFired(br.thejungle.species.senses.SenseEvent)
	 */
	public void senseFired(SenseEvent event) {
		// this species had seen anything
		if(event instanceof SightEvent) {
			
			SightEvent e = (SightEvent)event;
			
			// is it a species?
			if(e.getThingInfo() instanceof SpeciesInfo) {
				SpeciesInfo speciesInfo = (SpeciesInfo)e.getThingInfo();
				//if(walking) return;
				if(speciesInfo.isMale()!=isMale()) {
					//if(partnerInfo!=null) return;
					logger.finer(getIndividualID() + ": Going to partner position to reproduce");
					getAbilityWalk().gotoThing(speciesInfo);
					getAbilityReproduce().scheduleRequestReproduction(speciesInfo);
					this.partnerInfo = speciesInfo;
				}
			}
			
			// is it food?
			if(e.getThingInfo() instanceof PlantInfo) {
				PlantInfo plantInfo = (PlantInfo)e.getThingInfo();
				logger.info(getIndividualID() + ": I saw food at x=" + plantInfo.getXPos() + "; y=" + plantInfo.getYPos());
				getAbilityWalk().gotoPos(plantInfo.getXPos(), plantInfo.getYPos());
				getAbilityEat().schedulePerform();
				logger.info(getIndividualID() + ": Going to food position to eat it");
			}
			
		} else if(event instanceof ReproductionRequestEvent) {

			ReproductionRequestEvent rre = (ReproductionRequestEvent)event;
			boolean success = getAbilityReproduce().perform(rre.getVoucher());
			this.partnerInfo = null;
			logger.info(getIndividualID() + ": Accepted request for reproduction. success=" + success);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.SpeciesBrain#taskPerformed(br.thejungle.environment.Task)
	 */
	public void taskPerformed(Task task) {

	    if(!task.wasSuccessful()) {
	        logger.fine("Task not successful: " + task);
	        return;
	    }
		if(task instanceof WalkTask) {
			WalkTask wt = (WalkTask)task;
			logger.fine(getIndividualID() + ": " + wt + " completed");

		} else if(task instanceof EatTask) {
			logger.fine(getIndividualID() + ": Eating completed. Food=" + getFood());
			
		} else if(task instanceof RequestReproductionTask) {
			logger.fine(getIndividualID() + ": RequestReproductionTask completed. success="+task.wasSuccessful());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.SpeciesBrain#getSpeciesName()
	 */
	public String getSpeciesName() {
		return "SimpleGuy";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.SpeciesBrain#getNumberOfCustomGenes()
	 */
	public short getNumberOfCustomGenes() {
		return 0;
	}

}
