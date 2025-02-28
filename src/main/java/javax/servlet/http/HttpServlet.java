package javax.servlet.http;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;

import javax.servlet.Servlet;

public abstract class HttpServlet implements Servlet {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void service(Request request, Response response) {
        String method = request.getMethod();
        if (method.equals(METHOD_GET)) {
            doGet(request, response);
        } else if (method.equals(METHOD_POST)) {
            doPost(request, response);
        }
    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);
}
