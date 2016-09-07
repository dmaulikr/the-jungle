package br.thejungle.environment.things;

import java.io.Serializable;

/**
 * This is an abstract class for all things in the jungle
 */
public abstract class Thing implements Serializable {

    private long xPos, yPos, zPos;
    
    /**
     * @return Returns the xPos.
     */
    public long getXPos() {
        return xPos;
    }
    /**
     * @param pos The xPos to set.
     */
    public void setXPos(long pos) {
        xPos = pos;
    }
    /**
     * @return Returns the yPos.
     */
    public long getYPos() {
        return yPos;
    }
    /**
     * @param pos The yPos to set.
     */
    public void setYPos(long pos) {
        yPos = pos;
    }
    /**
     * @return Returns the zPos.
     */
    public long getZPos() {
        return zPos;
    }
    /**
     * @param pos The zPos to set.
     */
    public void setZPos(long pos) {
        zPos = pos;
    }
    
    public abstract void timeElapsed();

    public abstract boolean isValid();

    public String toString() {
        return "xPos="+xPos+"; yPos="+yPos+"; zPos="+zPos;
    }
    
}
