package cn.myth.tomcat.catalina.startup;

public interface Lifecycle {

    /* 针对控制流程 */

    /**
     * 初始化容器
     */
    void init() throws LifecycleException;

    /**
     * 启动容器
     */
    void start() throws LifecycleException;

    /**
     * 停止容器
     */
    void stop() throws LifecycleException;

    /**
     * 销毁容器
     */
    void destroy() throws LifecycleException;
}
