package cn.myth.tomcat.servlet;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;
import cn.myth.tomcat.util.HttpProtocolUtil;

import java.io.IOException;

public class MyTestServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>My Test Servlet Get</h1>";
        System.out.println(content);
        try {
            response.output(HttpProtocolUtil.getHttp200header(content.getBytes().length) + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>My Test Servlet Post</h1>";
        System.out.println(content);
        try {
            response.output(HttpProtocolUtil.getHttp200header(content.getBytes().length) + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
