package cn.myth.tomcat.catalina;

/**
 * Host
 * 虚拟主机
 * 通常用不到，因为当Tomcat与Apache/Nginx等外部Web服务器集成时，Web服务器通常已经根据请求的URL或其他信息确定了应该由哪个Context或Wrapper来处理请求
 */
public interface Host extends Container {

    /**
     * 设置关联的引擎
     * @param engine
     */
    void setEngine(Engine engine);

    /**
     * 获取引擎
     * @return
     */
    Engine getEngine();

    /**
     * 添加上下文
     * @param context
     */
    void addContext(Context context);

    /**
     * 获取上下文
     * @return
     */
    Context[] getContexts();

    /**
     * 设置应用程序根目录
     * Set the application root for this Host.  This can be an absolute
     * pathname, a relative pathname, or a URL.
     *
     * @param appBase The new application root
     */
    void setAppBase(String appBase);

    /**
     * 获取应用程序根目录
     * @return the application root for this Host.  This can be an absolute
     * pathname, a relative pathname, or a URL.
     */
    String getAppBase();


}
