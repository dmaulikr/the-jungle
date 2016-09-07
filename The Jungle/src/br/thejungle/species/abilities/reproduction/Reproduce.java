package br.thejungle.species.abilities.reproduction;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import br.thejungle.environment.Developer;
import br.thejungle.environment.things.Thing;
import br.thejungle.exceptions.SpeciesNotFoundException;
import br.thejungle.species.Genetics;
import br.thejungle.species.Species;
import br.thejungle.species.SpeciesBrain;
import br.thejungle.species.SpeciesInfo;
import br.thejungle.species.abilities.Ability;
import br.thejungle.util.MathUtil;

/**
 * Here is implemented the ability of reproducting
 */
public class Reproduce extends Ability {

    public static short MAX_CHILDREN = 20;

    public static short MAX_REPRODUCTION_REQUESTS = 20;
    
	private static Logger logger = Logger.getLogger("br.thejungle.species.abilities.reproduction.Reproduce");
	private static Random random = new Random();
	
	private CopyOnWriteArrayList<Voucher> reproductionRequests; 
    
	/**
	 * @param species
	 */
	public Reproduce(Species species) {
		super(species);
		reproductionRequests = new CopyOnWriteArrayList<Voucher>();
	}

	/**
	 * Reproduces with an partner. This method is assyncronous.
	 */
	public void scheduleRequestReproduction(SpeciesInfo partner) {
	    RequestReproductionTask t = new RequestReproductionTask(species, partner);
	    species.getJungle().executeTask(species.getSpeciesBrain(), t);
	}
	
	public boolean requestReproduction(SpeciesInfo partner) {
		
		if(partner.isMale()==species.isMale()) return false;
		
		//create and fire an event informing this species wants to reproduce with a partner and give to it a voucher
		ReproductionRequestEvent rqe = new ReproductionRequestEvent(species.getSpeciesInfo());
		reproductionRequests.add(rqe.getVoucher());

		//ensure max reproduction requests in memory
		if(reproductionRequests.size()>MAX_REPRODUCTION_REQUESTS) 
			reproductionRequests.remove(MAX_REPRODUCTION_REQUESTS);

		try {
			species.getJungle().findSpeciesByInfo(partner).getSpeciesBrain().senseFired(rqe);

		} catch (SpeciesNotFoundException e) {
    		logger.throwing("Reproduce", "requestReproduction()", e);
			return false;
		}

		return true;
	}

	/**
	 * Reproduces with an partner. This method is syncronous.
	 */
	public boolean perform(Voucher voucher) {

		try {
			//verify if the requester has a valid voucher to reproduce with me!
			Species partnerSpecies = species.getJungle().findSpeciesByInfo(voucher.getSpeciesInfo());
			if(partnerSpecies.getAbilityReproduce().getReproductionRequests().contains(voucher)) {
		
				partnerSpecies.getAbilityReproduce().getReproductionRequests().remove(voucher);
				
			} else {
				
				logger.finer("Couldn't reproduce because the voucher is invalid");
				return false;
			}
			
			SpeciesInfo partnerInfo = voucher.getSpeciesInfo();
	
			// verify if near me
			double distance = MathUtil.getDistance(species.getSpeciesInfo(), partnerInfo);
			if(distance > species.getSize() && distance > partnerSpecies.getSize()) {
				logger.finer("Couldn't reproduce because the partner is too far: distance="+ distance);
				return false;
			}
		    
	        Developer developer = species.getDeveloper();
	        int numberOfChildren = Math.min(species.getSpeciesInfo().getMaxChildren(), partnerInfo.getMaxChildren());
	        logger.finest(species.getIndividualID() + ": Generating " + numberOfChildren + " children");
	
	        if(numberOfChildren==0) logger.finer(species.getIndividualID() + ": No species will be created by this trial");
	        
	        boolean successful = false;
	        for(int i=0; i<numberOfChildren; i++) {
	            try {
		            Genetics newGenetics = species.getGenetics().combine(partnerSpecies.getGenetics(), species.getJungle().getMutationRate());
		            Species childSpecies = new Species(developer, species.getSpeciesBrain().getClass(), newGenetics);
		            
		            //consume food from parents
		            short qttyFood = (short)((newGenetics.getSize()*Species.MAX_SIZE)*(SpeciesBrain.OPTIMUM_NUTRITION + Species.VAR_NUTRITION)/2);
		            if(species.getFood()<qttyFood || partnerSpecies.getFood()<qttyFood) {
		            	logger.finer(species.getIndividualID() + ": Couldn't reproduce because the parents don't have enough food");
		            	continue;
		            }
		            species.consumeFood(qttyFood);
		            partnerSpecies.consumeFood(qttyFood);
		            
		            species.getJungle().acceptSpecies(childSpecies);
	                logger.fine(species.getIndividualID() + ": Reproduction successful. New species: " + childSpecies.getIndividualID());
		            successful = true;
	
	            } catch (InstantiationException e) {
	                logger.throwing("Reproduce", "perform(SpeciesInfo partner)", e);
	                return false;
	                
	            } catch (IllegalAccessException e) {
	                logger.throwing("Reproduce", "perform(SpeciesInfo partner)", e);
	                return false;
	            }
		    }
	        return successful;
	        
        } catch (SpeciesNotFoundException e) {
            logger.warning(e.getMessage());
            return false;
        }
        
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#perform()
	 */
	public boolean perform() {
		CopyOnWriteArrayList<Thing> things = species.getJungle().getThings(species.getXPos(), species.getYPos(), species.getSize());
		for (Thing thing : things) {
			if (thing instanceof Species) {
			    Species partner = (Species) thing;
			    Species chosenPartner = null;
			    short maxChildren = 0;
                logger.finest(species.getIndividualID() + ": Choosing best partner for reproduction");
			    if(partner.getAbilityReproduce().isMale() != species.getAbilityReproduce().isMale()) {
			        if(partner.getMaxChildren()>maxChildren) {
			            maxChildren = partner.getMaxChildren();
			            chosenPartner = partner;
			        }
			    }
			    if(chosenPartner!=null) {
		    		return requestReproduction(chosenPartner.getSpeciesInfo());
			    }
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.thejungle.species.abilities.Ability#isEffective()
	 */
	public boolean isEffective() {
		return species.getGenetics().getAbilityReproduce() >= 0.2;
	}
	
	public boolean isMale() {
	    return (species.getGenetics().getAbilityReproduce()<=0.5F);
	}
	
	public boolean isFemale() {
	    return !isMale();
	}
	
	// This is the max number of children this species can generate
	// Upon reproduction this will be randomized from 0 to this max value
	public short getMaxChildren() {
	    float factor = Math.abs(species.getGenetics().getAbilityReproduce()-0.5F)*2;
	    return (short)(factor*MAX_CHILDREN);
	}
	
	private CopyOnWriteArrayList<Voucher> getReproductionRequests() {
		return reproductionRequests;
	}
}
