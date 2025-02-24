package cn.myth.tomcat.catalina.util;

import cn.myth.tomcat.catalina.startup.Lifecycle;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LifecycleBase implements Lifecycle {

    private static final Logger log = LoggerFactory.getLogger(LifecycleBase.class);

    @Override
    public final synchronized void init() throws LifecycleException {
        log.info("{}初始化中...", this.getClass().getName());
        initInternal();
        log.info("{}初始化完成.", this.getClass().getName());
    }

    @Override
    public final synchronized void start() throws LifecycleException {
        log.info("{}启动中...", this.getClass().getName());
        startInternal();
        log.info("{}启动完成.", this.getClass().getName());
    }

    @Override
    public final synchronized void stop() throws LifecycleException {
        log.info("{}停止中...", this.getClass().getName());
        log.info("{}停止完成.", this.getClass().getName());
    }

    @Override
    public final synchronized void destroy() throws LifecycleException {
        log.info("{}销毁中...", this.getClass().getName());
        log.info("{}销毁完成.", this.getClass().getName());
    }


    /**
     * 内部初始化
     * Sub-classes implement this method to perform any instance initialisation
     * required.
     *
     * @throws LifecycleException If the initialisation fails
     */
    protected abstract void initInternal() throws LifecycleException;

    /**
     * 内部启动
     */
    protected abstract void startInternal() throws LifecycleException;

}
