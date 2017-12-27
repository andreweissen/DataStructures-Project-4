/**
 * CycleOccurrenceException.java - User-defined checked exception
 * Begun 12/04/17
 * @author Andrew Eissen
 */

package commandlinecompiler;

class CycleOccurrenceException extends Exception {

    /**
     * Default constructor
     */
    public CycleOccurrenceException() {
        super();
    }

    /**
     * Parameterized constructor
     * @param message
     */
    public CycleOccurrenceException(String message) {
       super(message);
    }

    /**
     * Parameterized constructor
     * @param message
     * @param cause
     */
    public CycleOccurrenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor
     * @param cause
     */
    public CycleOccurrenceException(Throwable cause) {
        super(cause);
    }
}