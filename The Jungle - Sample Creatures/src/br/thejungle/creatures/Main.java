package br.thejungle.creatures;

import java.io.IOException;
import java.util.logging.Logger;

import br.thejungle.creatures.simpleGuy.SimpleGuyBrain;
import br.thejungle.environment.Developer;
import br.thejungle.environment.Nature;

/*
 * Created on 30/11/2004
 */

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class Main {

	private static Logger logger = Logger.getLogger("br.thejungle.creatures.Main");

	public static void main(String[] args) throws IOException {

		Nature nature = Nature.getInstance();

		try {

			if (nature.getJungles().size() < 1) {
				nature.createJungle("Test1", 300, 300, 0.01F);
			}

			while(nature.countTotalSpecies() < 5) {
				nature.createSpecies("Test1", new Developer(), SimpleGuyBrain.class);
			}

			nature.startup();

			Thread.sleep(6000);

			/*
			 * String input = ""; Scanner sc = new Scanner(System.in);
			 * sc.useDelimiter("\n"); while(input.equals("")) { input =
			 * sc.next(); } sc.close();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		nature.shutdown();
		//nature.saveCurrentState();

		System.out.println(nature);

	}

}
