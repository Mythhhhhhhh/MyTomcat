package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.*;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardHost extends ContainerBase implements Host {

    private static final Logger log = LoggerFactory.getLogger(StandardHost.class);

    private Engine engine;

    private Context[] contexts = new Context[0];
    private final Object contextsLock = new Object();

    private String appBase = "webapps";

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Engine getEngine() {
        return this.engine;
    }

    @Override
    public void addContext(Context context) {
        Context[] result = new Context[contexts.length+1];
        System.arraycopy(this.contexts,0,result,0,this.contexts.length);
        result[contexts.length] = context;
        this.contexts = result;
    }

    @Override
    public Context[] getContexts() {
        return contexts;
    }

    @Override
    public void setAppBase(String appBase) {
        if (appBase.trim().equals("")) {
            log.warn("Using an empty string for appBase on host {} will set it to CATALINA_BASE, which is a bad idea", getName());
        }
        this.appBase = appBase;
    }

    @Override
    public String getAppBase() {
        return appBase;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        for (Context context : contexts) {
            try {
                context.init();
            } catch (Exception e) {
                log.error("Failed to initialize context {}", context, e);
            }
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        synchronized (contextsLock) {
            for (Context context : contexts) {
                try {
                    context.start();
                } catch (Exception e) {
                    log.error("Failed to start context {}", context, e);
                }
            }
        }
    }
}
