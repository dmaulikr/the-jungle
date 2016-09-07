/*
 * Created on 30/11/2004
 */
package br.thejungle.exceptions;

/**
 * @author Fl�vio Stutz (flaviostutz@uol.com.br)
 */
public class BrainNotInstantiableException extends Exception {

    /**
     * 
     */
    public BrainNotInstantiableException() {
        super();
    }

    /**
     * @param arg0
     */
    public BrainNotInstantiableException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public BrainNotInstantiableException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public BrainNotInstantiableException(Throwable arg0) {
        super(arg0);
    }

}
