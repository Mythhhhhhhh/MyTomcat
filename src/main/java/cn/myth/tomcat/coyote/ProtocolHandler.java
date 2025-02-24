package cn.myth.tomcat.coyote;

import java.util.concurrent.Executor;

/**
 * Abstract the protocol implementation, including threading, etc.
 *
 * This is the main interface to be implemented by a coyote protocol.
 * Adapter is the main interface to be implemented by a coyote servlet
 * container.
 *
 * @see Adapter
 */
public interface ProtocolHandler {

    /**
     * Return the adapter associated with the protocol handler.
     * @return the adapter
     */
    Adapter getAdapter();

    /**
     * The adapter, used to call the connector.
     *
     * @param adapter The adapter to associate
     */
    void setAdapter(Adapter adapter);

    /**
     * The executor, provide access to the underlying thread pool.
     *
     * @return The executor used to process requests
     */
    Executor getExecutor();

    /**
     * Initialise the protocol.
     *
     * @throws Exception If the protocol handler fails to initialise
     */
    void init() throws Exception;

    /**
     * Start the protocol.
     *
     * @throws Exception If the protocol handler fails to start
     */
    void start() throws Exception;

}
