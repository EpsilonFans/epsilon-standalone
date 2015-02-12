package org.eclipse.epsilon.standalone.util;

/**
 * The Class EpsilonStandaloneException.
 */
public class ParseException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4172842347806568754L;
	
	/**
	 * Instantiates a new epsilon standalone exception.
	 *
	 * @param msg the msg
	 */
	public ParseException(String msg){
		super(msg);
	}

    /**
     * Instantiates a new epsilon standalone exception.
     *
     * @param msg the msg
     * @param t the previous Throwable cause
     */
    public ParseException(String msg, Throwable t){
    	super(msg,t);
    } 

}
