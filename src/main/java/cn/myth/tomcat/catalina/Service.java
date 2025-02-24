package cn.myth.tomcat.catalina;

import cn.myth.tomcat.catalina.connector.Connector;
import cn.myth.tomcat.catalina.startup.Lifecycle;

/**
 * A <strong>Service</strong> is a group of one or more
 * <strong>Connectors</strong> that share a single <strong>Container</strong>
 * to process their incoming requests.  This arrangement allows, for example,
 * a non-SSL and SSL connector to share the same population of web apps.
 * <p>
 * A given JVM can contain any number of Service instances; however, they are
 * completely independent of each other and share only the basic JVM facilities
 * and classes on the system class path.
 */
public interface Service extends Lifecycle {

    /**
     * Set the <code>Server</code> with which we are associated (if any).
     *
     * @param server The server that owns this Service
     */
    void setServer(Server server);

    /**
     * @return the <code>Server</code> with which we are associated (if any).
     */
    Server getServer();

    // 一个Service包含一个Container和多个Connector

    /**
     * Set the <code>Engine</code> that handles requests for all
     * <code>Connectors</code> associated with this Service.
     *
     * @param engine The new Engine
     */
    void setContainer(Engine engine);

    /**
     * @return the <code>Engine</code> that handles requests for all
     * <code>Connectors</code> associated with this Service.
     */
    Engine getContainer();

    void addConnectors(Connector connector);

}
