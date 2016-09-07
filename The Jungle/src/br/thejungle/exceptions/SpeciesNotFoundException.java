/*
 * Created on 30/11/2004
 */
package br.thejungle.exceptions;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class SpeciesNotFoundException extends Exception {

	/**
	 * 
	 */
	public SpeciesNotFoundException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public SpeciesNotFoundException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SpeciesNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public SpeciesNotFoundException(Throwable arg0) {
		super(arg0);
	}
}
