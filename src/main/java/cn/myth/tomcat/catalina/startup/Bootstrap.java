package cn.myth.tomcat.catalina.startup;

import cn.myth.tomcat.util.HttpProtocolUtil;

import java.io.IOException;
import java.io.OutputStream;
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
         * 1.0版本
         * 需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面“Hello MyTomcat!”
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>MyTomcat start on port: " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello MyTomcat!";
            String responseText = HttpProtocolUtil.getHttp200header(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
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
