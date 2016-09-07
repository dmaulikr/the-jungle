/*
 * Created on 14/12/2004
 */
package br.thejungle.view;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;

import br.thejungle.creatures.simpleGuy.SimpleGuyBrain;
import br.thejungle.environment.Developer;
import br.thejungle.environment.INature;
import br.thejungle.environment.Jungle;
import br.thejungle.environment.Nature;
import br.thejungle.environment.things.Plant;
import br.thejungle.environment.things.Thing;
import br.thejungle.exceptions.BrainNotInstantiableException;
import br.thejungle.exceptions.JungleNotFoundException;
import br.thejungle.species.Species;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class JungleView extends Applet implements Runnable, MouseListener, MouseMotionListener {

    Thread thread;
    boolean active;
    final long REPAINT_DELAY = 100;
    INature nature;
    String jungleName;
    
    public void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
        nature = Nature.getInstance();
        //jungleName = getParameter("jungleName");
        
        // test purposes
        try {
            jungleName = "Test1";
            
			if (nature.getJungles().size() < 1) {
				nature.createJungle(jungleName, 400, 400, 0.001F);
			}
			while(nature.countTotalSpecies()<5) {
				nature.createSpecies(jungleName, new Developer(), SimpleGuyBrain.class);
			}
			((Nature)nature).startup();
			
        } catch (Exception e) {
            showStatus("Error on init(): " + e.getMessage());
        }
        
    }
    
    public void start() {
        thread = new Thread(this);
        active = true;
        thread.start();
    }
    
    public void run() {
        while(active) {
            repaint();
    		try {Thread.sleep(REPAINT_DELAY);} catch (InterruptedException e){}
        }
    }
    
    public void paint(Graphics g) {
        try {
        	//Image offImg = createImage(getWidth(), getHeight());
        	//Graphics offGraphics = offImg.getGraphics();
        	
            drawThings(g);
            
            //g.drawImage(offImg, 0, 0, this);
            
        } catch (Exception e) {
            showStatus("Error on paint(): " + e.getMessage());
        }
    }
    
    private void drawThings(Graphics g) throws RemoteException, JungleNotFoundException {

        for(Thing thing: nature.getAllThings(jungleName)) {
            
            // draw species
            if(thing instanceof Species) {
            	Species species = (Species)thing;
                g.setColor(Color.RED);
                float size = (species.getSize()*2);
                int x = (int)(species.getXPos() - (size/2));
                int y = (int)(species.getYPos() - (size/2));
                g.fillOval(x, y, (int)size, (int)size);

                g.setColor(Color.LIGHT_GRAY);
                size = (int)(species.getSightSense().getRange())*2;
                x = (int)(species.getXPos() - (size/2));
                y = (int)(species.getYPos() - (size/2));
                g.drawOval(x, y, (int)size, (int)size);
                
                g.setColor(Color.DARK_GRAY);
                g.drawString((species.isMale()?"M":"F"), (int)species.getXPos(), (int)species.getYPos());
            }

            // draw plants
            if(thing instanceof Plant) {
            	Plant plant = (Plant)thing;
                g.setColor(Color.GREEN);
                int width = (plant.getSize()*2);
                int height = width;
                int x = (int)plant.getXPos() - (width/2);
                int y = (int)plant.getYPos() - (height/2);
                g.fillOval(x, y, width, height);
            }
            
            g.setColor(Color.BLACK);
            Jungle jungle = nature.getJungles().get(jungleName);
            g.drawString("Species: " + jungle.countSpecies(), 10, 10);
            g.drawString("Plants: " + jungle.countPlants(), 10, 25);

        }

    }

    public void stop() {
        active = false;
    }
    
    public void destroy() {
		((Nature)nature).shutdown();
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    	try {
			nature.createSpecies(jungleName, new Developer(), SimpleGuyBrain.class);

    	} catch (RemoteException e) {
			e.printStackTrace();
		} catch (JungleNotFoundException e) {
			e.printStackTrace();
		} catch (BrainNotInstantiableException e) {
			e.printStackTrace();
		}
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseDragged(MouseEvent arg0) {
    }
    
    public void mouseExited(MouseEvent arg0) {
    }

    
}
