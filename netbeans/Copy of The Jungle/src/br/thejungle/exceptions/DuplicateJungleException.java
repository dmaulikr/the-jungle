/*
 * Created on 30/11/2004
 */
package br.thejungle.exceptions;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class DuplicateJungleException extends Exception {

    /**
     * 
     */
    public DuplicateJungleException() {
        super();
    }

    /**
     * @param arg0
     */
    public DuplicateJungleException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public DuplicateJungleException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public DuplicateJungleException(Throwable arg0) {
        super(arg0);
    }

}
