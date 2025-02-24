package cn.myth.tomcat.catalina.connector;

import cn.myth.tomcat.catalina.Service;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import cn.myth.tomcat.catalina.util.LifecycleBase;
import cn.myth.tomcat.coyote.Adapter;
import cn.myth.tomcat.coyote.ProtocolHandler;
import cn.myth.tomcat.util.IntrospectionUtils;

/**
 * Implementation of a Coyote connector.
 */
public class Connector extends LifecycleBase {

    /**
     * 端口
     * The port number on which we listen for requests.
     */
    private int port = -1;

    /**
     * The <code>Service</code> we are associated with (if any).
     */
    private Service service;


    /**
     * Coyote protocol handler.
     */
    protected final ProtocolHandler protocolHandler;

    /**
     * Coyote adapter.
     */
    protected Adapter adapter = null;


    public Connector(ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }


    @Override
    protected void initInternal() throws LifecycleException {
        // Initialize adapter
        adapter = new CoyoteAdapter(this);
        protocolHandler.setAdapter(adapter);

        try {
            protocolHandler.init();
        } catch (Exception e) {
            throw new LifecycleException("Protocol handler initialization failed", e);
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        // Validate settings before starting
        if (getPort() < 0) {
            throw new LifecycleException(
                    "The connector cannot start since the specified port value of " + getPort() + " is invalid");
        }

        try {
            protocolHandler.start();
        } catch (Exception e) {
            throw new LifecycleException("Protocol handler start failed", e);
        }

    }


    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
        setProperty("port", String.valueOf(port));
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Set a property on the protocol handler.
     *
     * @param name the property name
     * @param value the property value
     * @return <code>true</code> if the property was successfully set
     */
    public boolean setProperty(String name, String value) {
        return IntrospectionUtils.setProperty(protocolHandler, name, value);
    }

}
