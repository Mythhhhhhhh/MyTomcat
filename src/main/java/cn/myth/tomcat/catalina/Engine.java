package cn.myth.tomcat.catalina;

/**
 * Engine
 * 负责处理请求的顶层容器，代表整个Servlet引擎（Catalina）
 * 通常用不到，因为当Tomcat与Apache/Nginx等外部Web服务器集成时，Web服务器通常已经根据请求的URL或其他信息确定了应该由哪个Context或Wrapper来处理请求。
 * 因此，Tomcat的Engine不需要再参与请求的路由决策
 */
public interface Engine extends Container {

    /**
     * Set the <code>Service</code> with which we are associated (if any).
     *
     * @param service The service that owns this Engine
     */
    void setService(Service service);

    /**
     * @return the <code>Service</code> with which we are associated (if any).
     */
    Service getService();

    /**
     * 设置一组Host
     * @param hosts
     */
    void setHosts(Host[] hosts);

    /**
     * 获取一组host
     * @return
     */
    Host[] getHosts();
}
