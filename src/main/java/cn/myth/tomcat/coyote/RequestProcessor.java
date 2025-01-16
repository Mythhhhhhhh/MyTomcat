package cn.myth.tomcat.coyote;

import cn.myth.tomcat.servlet.HttpServlet;

import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private final Socket socket;
    private final Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                // 动态servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
