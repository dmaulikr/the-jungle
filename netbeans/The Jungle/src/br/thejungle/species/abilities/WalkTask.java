/*
 * WalkTask.java
 *
 * Created on 6 de Dezembro de 2004, 23:10
 */

package br.thejungle.species.abilities;

import br.thejungle.environment.Task;
import br.thejungle.species.Species;
import br.thejungle.util.MathUtil;
import java.util.logging.Logger;

/**
 *
 * @author Flávio
 */
public class WalkTask implements Task {
    
    private short speed;
    private long xPos, yPos;
    private float directionAngle;
    private long distance;
    private short action;
    private Species species;
    private static final short GOTOXY = 1;
    private static final short DIRECTIONAL = 2;
    private long walked;
    private boolean completed;
    
    private static Logger logger = Logger.getLogger("br.thejungle.abilities.WalkTask");
    
    /** Creates a new instance of WalkTask */
    public WalkTask(Species species, long xPos, long yPos, short speed) {
        action = GOTOXY;
        this.xPos = xPos;
        this.yPos = yPos;
        this.species = species;
        this.walked = 0;
        this.speed = speed;
        this.completed = false;
    }
    
    public WalkTask(Species species, float directionAngle, long distance, short speed) {
        action = DIRECTIONAL;
        this.directionAngle = directionAngle;
        this.distance = distance;
        this.species = species;
        this.speed = speed;
        this.completed = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#stepWork()
     */
    public void stepWork() {
        double angle = 0;
        if(action==GOTOXY) {
            angle = MathUtil.getAngle(species.getXPos(), species.getYPos(), xPos, yPos);
            if(MathUtil.getDistance(species.getXPos(), species.getYPos(), xPos, yPos)<=species.getSize()) {
                species.setXPos(xPos);
                species.setYPos(yPos);
                completed = true;
            }
        } else if(action==DIRECTIONAL) {
            angle = directionAngle;
            if(walked>=distance) completed = true;
        } else {
            logger.warning("action in WalkTask isn't GOTOXY nor DIRECTION. It's value is " + action);
        }
        walked += MathUtil.getDistance(species.getXPos(), species.getYPos(), xPos, yPos);
        species.setXPos((long)(species.getXPos()*(1+(speed*Math.cos(angle)))));
        species.setYPos((long)(species.getYPos()*(1+(speed*Math.sin(angle)))));
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see br.thejungle.environment.Task#isCompleted()
     */
    public boolean isCompleted() {
        return completed;
    }
    
    public String getIndividualID() {
        return species.getIndividualID();
    }


}
