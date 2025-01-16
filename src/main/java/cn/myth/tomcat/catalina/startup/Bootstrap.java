package cn.myth.tomcat.catalina.startup;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;
import cn.myth.tomcat.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Bootstrap {

    /* 定义socket监听的端口号 */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * MyTomcat启动需要初始化展开的一些操作
     */
    public void start() throws IOException {
        // 加载解析相关的配置 web.xml
        loadServlet();

        /**
         * 3.0版本
         * 需求：可以请求动态资源(Servlet)
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>MyTomcat start on port: " + port);

        while (true) {
            Socket socket = serverSocket.accept();

            // 封装Request对象和Response对象
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
            socket.close();
        }
    }

    /**
     * MyTomcat 启动入口
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动MyTomcat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (Element element : selectNodes) {
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

                // 根据servlet-name的值找到url-pattern
                Element servletMappingElement = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMappingElement.selectSingleNode("url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
