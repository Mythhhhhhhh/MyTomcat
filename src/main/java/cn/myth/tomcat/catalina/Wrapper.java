package cn.myth.tomcat.catalina;

import cn.myth.tomcat.servlet.Servlet;

/**
 * Wrapper
 * 单个servlet
 */
public interface Wrapper extends Container {

    /**
     * @return the associated Servlet instance.
     */
    Servlet getServlet();

    /**
     * Set the associated Servlet instance
     *
     * @param servlet The associated Servlet
     */
    void setServlet(Servlet servlet);

    /**
     * @return the fully qualified servlet class name for this servlet.
     */
    String getServletClass();

    /**
     * Set the fully qualified servlet class name for this servlet.
     *
     * @param servletClass Servlet class name
     */
    void setServletClass(String servletClass);

    /**
     * 设置UrlPattern
     * @param urlPattern
     */
    void setUrlPattern(String urlPattern);

    /**
     * 获取UrlPattern
     * @return
     */
    String getUrlPattern();

    /**
     * 设置上下文
     * @param context
     */
    void setContext(Context context);
}
