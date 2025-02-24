package cn.myth.tomcat.util.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 默认的ServerSocket工厂
 * Default server socket factory. Doesn't do much except give us
 * plain old server sockets.
 */
public class DefaultServerSocketFactory implements ServerSocketFactory {
    @Override
    public ServerSocket createSocket(int port) throws IOException, InstantiationException {
        return new ServerSocket(port);
    }

    @Override
    public Socket acceptSocket(ServerSocket socket) throws IOException {
        return socket.accept();
    }
}
