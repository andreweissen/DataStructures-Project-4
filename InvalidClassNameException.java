/**
 * InvalidClassNameException.java - User-defined checked exception
 * Begun 12/04/17
 * @author Andrew Eissen
 */

package commandlinecompiler;

class InvalidClassNameException extends Exception {

    /**
     * Default constructor
     */
    public InvalidClassNameException() {
        super();
    }

    /**
     * Parameterized constructor
     * @param message
     */
    public InvalidClassNameException(String message) {
       super(message);
    }

    /**
     * Parameterized constructor
     * @param message
     * @param cause
     */
    public InvalidClassNameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor
     * @param cause
     */
    public InvalidClassNameException(Throwable cause) {
        super(cause);
    }
}