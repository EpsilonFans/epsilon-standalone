package org.eclipse.epsilon.standalone.util;

/**
 * The Class EpsilonStandaloneException.
 */
public class ExecutionException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4172842347806568754L;
	
	/**
	 * Instantiates a new epsilon standalone exception.
	 *
	 * @param msg the msg
	 */
	public ExecutionException(String msg){
		super(msg);
	}

    /**
     * Instantiates a new epsilon standalone exception.
     *
     * @param msg the msg
     * @param t the previous Throwable cause
     */
    public ExecutionException(String msg, Throwable t){
    	super(msg,t);
    } 

}
