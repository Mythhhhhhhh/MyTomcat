package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Server;
import cn.myth.tomcat.catalina.Service;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import cn.myth.tomcat.catalina.util.LifecycleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StandardServer extends LifecycleBase implements Server {

    private static final Logger log = LoggerFactory.getLogger(StandardServer.class);

    /**
     * The set of Services associated with this Server.
     */
    private Service[] services = new Service[0];

    private final Object servicesLock = new Object();

    @Override
    public void addService(Service service) {
        service.setServer(this);
        synchronized (servicesLock) {
            Service[] results = new Service[services.length + 1];
            System.arraycopy(services, 0, results, 0, services.length);
            results[services.length] = service;
            services = results;

//            try {
//                service.start();
//            } catch (LifecycleException e) {
//                // Ignore
//            }

        }
    }

    @Override
    public Service[] findServices() {
        return services;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        // Initialize our defined Services
        for (int i = 0; i < services.length; i++) {
            services[i].init();
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        // Start our defined Services
        synchronized (servicesLock) {
            for (int i = 0; i < services.length; i++) {
                services[i].start();
            }
        }
    }

}
