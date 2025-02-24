package cn.myth.tomcat.util.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;

public class JIoEndpoint extends AbstractEndpoint<Socket> {

    private static final Logger log = LoggerFactory.getLogger(JIoEndpoint.class);

    // Associated server socket.
    protected ServerSocket serverSocket = null;

    // Server socket factory.
    protected ServerSocketFactory serverSocketFactory = null;

    // Handling of accepted sockets.
    protected Handler handler = null;
    public void setHandler(Handler handler ) { this.handler = handler; }
    public Handler getHandler() { return handler; }

    public interface Handler extends AbstractEndpoint.Handler {
        void process(SocketWrapper<Socket> socket);
    }

    protected class Acceptor extends AbstractEndpoint.Acceptor {
        @Override
        public void run() {
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocketFactory.acceptSocket(serverSocket);
                    // 处理socket
                    processSocket(socket);
                } catch (IOException | NullPointerException e) {
                    log.error("Socket accept failed", e);
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.error("Socket accept failed", t);
                }
            }
        }
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            // Ignore
        }
    }

    // Worker线程，处理Socket，只会在Executor中使用
    protected class SocketProcessor implements Runnable {

        protected SocketWrapper<Socket> socket;

        public SocketProcessor(SocketWrapper<Socket> socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            handler.process(socket);
            socket = null;
            // Finish up this request
        }
    }


    protected boolean processSocket(Socket socket) {
        // Process the request from this socket
        try {
            SocketWrapper socketWrapper = new SocketWrapper(socket);
            getExecutor().execute(new SocketProcessor(socketWrapper));
        } catch (RejectedExecutionException x) {
            log.warn("Socket processing request was rejected for:" + socket, x);
            return false;
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error("Error allocating socket processor", t);
            return false;
        }
        return true;
    }



    @Override
    public void bind() throws Exception {
        if (serverSocketFactory == null) {
            serverSocketFactory = new DefaultServerSocketFactory();
        }

        if (serverSocket == null) {
            serverSocket = serverSocketFactory.createSocket(getPort());
        }
    }

    @Override
    public void startInternal() throws Exception {
        // Create worker collection
        if (getExecutor() == null) {
            createExecutor();
        }

        startAcceptorThreads();
    }

    @Override
    protected AbstractEndpoint.Acceptor createAcceptor() {
        return new Acceptor();
    }


}
