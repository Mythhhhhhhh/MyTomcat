package cn.myth.tomcat.catalina;

/**
 * Context
 * servlet context, Web应用程序
 */
public interface Context extends Container {

    /**
     * 添加Servlet容器
     * @param wrapper
     */
    void addWrapper(Wrapper wrapper);

    /**
     * 查找Servlet
     * @param servletName
     * @return
     */
    Wrapper findWrapper(String servletName);

    /**
     * 设置web的class loader
     * @param classLoader
     */
    void setClassloader(ClassLoader classLoader);
}
