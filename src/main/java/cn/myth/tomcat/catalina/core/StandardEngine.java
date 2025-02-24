package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Engine;
import cn.myth.tomcat.catalina.Host;
import cn.myth.tomcat.catalina.Service;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardEngine extends ContainerBase implements Engine {

    private static final Logger log = LoggerFactory.getLogger(StandardEngine.class);

    private Service service = null;

    private Host[] hosts = new Host[0];
    private final Object hostsLock = new Object();

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public Host[] getHosts() {
        return hosts;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        for (Host host : hosts) {
            try {
                host.init();
            } catch (Exception e) {
                log.error("Failed to initialize host {}", host, e);
            }
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        synchronized (hostsLock) {
            for (Host host : hosts) {
                try {
                    host.start();
                } catch (Exception e) {
                    log.error("Failed to start host {}", host, e);
                }
            }
        }
    }
}
