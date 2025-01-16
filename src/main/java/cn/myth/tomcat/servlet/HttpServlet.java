package cn.myth.tomcat.servlet;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;

public abstract class HttpServlet implements Servlet {

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void service(Request request, Response response) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            doPost(request, response);
        }

    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);
}
