package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Engine;
import cn.myth.tomcat.catalina.Server;
import cn.myth.tomcat.catalina.Service;
import cn.myth.tomcat.catalina.connector.Connector;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import cn.myth.tomcat.catalina.util.LifecycleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardService extends LifecycleBase implements Service {

    private static final Logger log = LoggerFactory.getLogger(StandardService.class);

    private Server server;

    private Engine engine = null;

    private Connector[] connectors = new Connector[0];
    private final Object connectorsLock = new Object();


    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setContainer(Engine engine) {
        Engine oldEngine = this.engine;
        if (oldEngine != null) {
            oldEngine.setService(null);
        }
        this.engine = engine;
        if (this.engine != null) {
            this.engine.setService(this);
        }
    }

    @Override
    public Engine getContainer() {
        return engine;
    }

    @Override
    public void addConnectors(Connector connector) {
        synchronized (connectorsLock) {
            connector.setService(this);
            Connector results[] = new Connector[connectors.length + 1];
            System.arraycopy(connectors, 0, results, 0, connectors.length);
            results[connectors.length] = connector;
            connectors = results;
        }
    }


    @Override
    protected void initInternal() throws LifecycleException {
        if (engine != null) {
            engine.init();
        }
        // Initialize our defined Connectors
        synchronized (connectorsLock) {
            for (Connector connector : connectors) {
                try {
                    connector.init();
                } catch (Exception e) {
                    log.error("Failed to initialize connector {}", connector, e);
                }
            }
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        // Start our defined Container first
        if (engine != null) {
            synchronized (engine) {
                engine.start();
            }
        }

        // Start our defined Connectors second
        synchronized (connectorsLock) {
            for (Connector connector : connectors) {
                try {
                    connector.start();
                } catch (Exception e) {
                    log.error("Failed to start connector {}", connector, e);
                }
            }
        }
    }
}
