package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Context;
import cn.myth.tomcat.catalina.Wrapper;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import javax.servlet.Servlet;

public class StandardWrapper extends ContainerBase implements Wrapper {

    // servlet全类名
    private String servletClass;

    // url匹配路径
    private String urlPattern;

    // servlet
    private Servlet servlet;

    // 上下文
    private Context context;


    @Override
    public Servlet getServlet() {
        return servlet;
    }

    @Override
    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public String getServletClass() {
        return servletClass;
    }

    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    @Override
    public String getUrlPattern() {
        return urlPattern;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void initInternal() throws LifecycleException {

    }

    @Override
    protected void startInternal() throws LifecycleException {

    }

}
