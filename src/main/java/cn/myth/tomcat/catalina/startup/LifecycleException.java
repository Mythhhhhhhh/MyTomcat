package cn.myth.tomcat.catalina.startup;

/**
 * General purpose exception that is thrown to indicate a lifecycle related
 * problem.  Such exceptions should generally be considered fatal to the
 * operation of the application containing this component.
 */
public final class LifecycleException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a new LifecycleException with no other information.
     */
    private LifecycleException() {
        super();
    }

    /**
     * Construct a new LifecycleException for the specified message.
     *
     * @param message Message describing this exception
     */
    public LifecycleException(String message) {
        super(message);
    }

    /**
     * Construct a new LifecycleException for the specified throwable.
     *
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Construct a new LifecycleException for the specified message
     * and throwable.
     *
     * @param message Message describing this exception
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
