/*
 * Created on Dec 9, 2004
 */
package br.thejungle.exceptions;


/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 *
 */
public class JungleNotActiveException extends RuntimeException {

	/**
	 * 
	 */
	public JungleNotActiveException() {
		super();
	}
	/**
	 * @param message
	 */
	public JungleNotActiveException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public JungleNotActiveException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * @param cause
	 */
	public JungleNotActiveException(Throwable cause) {
		super(cause);
	}
}
