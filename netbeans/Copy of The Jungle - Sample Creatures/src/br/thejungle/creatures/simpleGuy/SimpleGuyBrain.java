/*
 * Created on 30/11/2004
 */
package br.thejungle.creatures.simpleGuy;

import br.thejungle.environment.Task;
import br.thejungle.species.SpeciesBrain;
import br.thejungle.species.senses.SenseEvent;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class SimpleGuyBrain extends SpeciesBrain {

    /* (non-Javadoc)
     * @see br.thejungle.species.SpeciesBrain#think()
     */
    public void think() {
        //System.out.println("Thinking...");
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.SpeciesBrain#senseFired(br.thejungle.species.senses.SenseEvent)
     */
    public void senseFired(SenseEvent event) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see br.thejungle.species.SpeciesBrain#taskPerformed(br.thejungle.environment.Task)
     */
    public void taskPerformed(Task task) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see br.thejungle.species.SpeciesBrain#getSpeciesName()
     */
    public String getSpeciesName() {
        return "SimpleGuy";
    }

    /* (non-Javadoc)
     * @see br.thejungle.species.SpeciesBrain#getNumberOfCustomGenes()
     */
    public short getNumberOfCustomGenes() {
        return 0;
    }

}
