package cn.myth.tomcat.util.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface ServerSocketFactory {

    ServerSocket createSocket(int port) throws IOException, InstantiationException;

    Socket acceptSocket(ServerSocket socket) throws IOException;
}
