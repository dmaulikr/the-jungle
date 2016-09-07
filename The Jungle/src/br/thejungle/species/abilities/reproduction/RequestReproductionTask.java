/*
 * Created on 12/12/2004
 */
package br.thejungle.species.abilities.reproduction;

import br.thejungle.environment.TaskSupport;
import br.thejungle.species.Species;
import br.thejungle.species.SpeciesInfo;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class RequestReproductionTask extends TaskSupport {

    private SpeciesInfo partner;

    /**
     * 
     */
    public RequestReproductionTask(Species species, SpeciesInfo partner) {
        super(species);
        this.partner = partner;
    }

    /* (non-Javadoc)
     * @see br.thejungle.environment.Task#stepWork()
     */
    public void stepWork() {
        successful = species.getAbilityReproduce().requestReproduction(partner);
        completed = true;
    }

}
