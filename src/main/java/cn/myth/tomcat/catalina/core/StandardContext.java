package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Context;
import cn.myth.tomcat.catalina.Wrapper;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import cn.myth.tomcat.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StandardContext extends ContainerBase implements Context {

    private static final Logger log = LoggerFactory.getLogger(StandardContext.class);

    private Map<String, Wrapper> wrapperMap = new ConcurrentHashMap<>();

    private ClassLoader classLoader;


    @Override
    public void addWrapper(Wrapper wrapper) {
        wrapperMap.put(wrapper.getUrlPattern(), wrapper);
    }

    @Override
    public Wrapper findWrapper(String servletName) {
        return wrapperMap.get(servletName);
    }

    @Override
    public void setClassloader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        Set<Map.Entry<String, Wrapper>> entries = wrapperMap.entrySet();
        for (Map.Entry<String, Wrapper> entry : entries) {
            Wrapper wrapper = entry.getValue();
            try {
                Class<?> servletClass = classLoader.loadClass(wrapper.getServletClass());
                Servlet servlet = (Servlet) servletClass.getDeclaredConstructor().newInstance();
                wrapper.setServlet(servlet);
                servlet.init();
            } catch (Exception e) {
                log.error("Failed to initialize wrapper {}", wrapper, e);
            }
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {

    }
}
