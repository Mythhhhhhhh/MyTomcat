package cn.myth.tomcat.catalina.startup;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        /**
         * 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>MyTomcat start on port: " + port);

        while (true) {
            Socket socket = serverSocket.accept();

            // 封装Request对象和Response对象
            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());

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

}
