/*
 * RmiMain.java
 *
 * Created on December 6, 2004, 6:20 PM
 */

package br.thejungle.creatures;

import br.thejungle.creatures.simpleGuy.SimpleGuyBrain;
import br.thejungle.environment.Developer;
import br.thejungle.environment.INature;
import br.thejungle.environment.Nature;
import java.rmi.Naming;


/**
 *
 * @author  f3310193
 */
public class RmiMain {
    
    public static void main(String[] args) {
        try {
            INature nature = (INature)Naming.lookup("rmi://localhost/" + Nature.RMI_NAME);
            System.out.println(nature);
            
            nature.createJungle("Test1", 100L, 100L, 0.01F);
            //nature.createSpecies("", new Developer(), SimpleGuyBrain.class);
            
            System.out.println(nature);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
}
