package cn.myth.tomcat.catalina;

import cn.myth.tomcat.catalina.startup.Lifecycle;

/**
 * 一个Server包含一个或多个Service
 * <p>
 *
 * A <code>Server</code> element represents the entire Catalina
 * servlet container.  Its attributes represent the characteristics of
 * the servlet container as a whole.  A <code>Server</code> may contain
 * one or more <code>Services</code>, and the top level set of naming
 * resources.
 * <p>
 * Normally, an implementation of this interface will also implement
 * <code>Lifecycle</code>, such that when the <code>start()</code> and
 * <code>stop()</code> methods are called, all of the defined
 * <code>Services</code> are also started or stopped.
 * <p>
 * In between, the implementation must open a server socket on the port number
 * specified by the <code>port</code> property.  When a connection is accepted,
 * the first line is read and compared with the specified shutdown command.
 * If the command matches, shutdown of the server is initiated.
 * <p>
 * <strong>NOTE</strong> - The concrete implementation of this class should
 * register the (singleton) instance with the <code>ServerFactory</code>
 * class in its constructor(s).
 *
 */
public interface Server extends Lifecycle {

    /**
     * Add a new Service to the set of defined Services.
     *
     * @param service The Service to be added
     */
    void addService(Service service);

    /**
     * @return the set of Services defined within this Server.
     */
    Service[] findServices();
}
