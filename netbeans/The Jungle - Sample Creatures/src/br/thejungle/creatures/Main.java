package br.thejungle.creatures;

import java.io.IOException;

import br.thejungle.creatures.simpleGuy.SimpleGuyBrain;
import br.thejungle.environment.Developer;
import br.thejungle.environment.Nature;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;


import java.util.logging.Logger;
 
/*
 * Created on 30/11/2004
 */

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class Main {
    
    private static Logger logger = Logger.getLogger("br.thejungle.creatures.Main");

    public static void main(String[] args) throws IOException {
        
        try {
            
            logger.setLevel(Level.INFO);
            for(Handler h: logger.getHandlers()) {
                h.setLevel(Level.ALL);
            }
            
            Nature nature = Nature.getInstance();
            
            System.out.println("Nature online");
            
            if(nature.getJungles().size()<1) {
                nature.createJungle("Test1", 500, 500, 0.01F);
            }
            
            if(nature.countTotalSpecies()<1) {
                nature.createSpecies("Test1", new Developer(), SimpleGuyBrain.class);
            }
            
            long start = System.currentTimeMillis();
            
            Thread.sleep(10000);
            
/*            String input = "";
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\n");
            while(input.equals("")) {
                input = sc.next();
            }
            sc.close();*/
            
            System.out.println(nature);
            
            nature.saveCurrentState();
            nature.shutdown();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Nature exit");
    }
    
}
