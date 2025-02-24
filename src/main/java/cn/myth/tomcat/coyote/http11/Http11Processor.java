package cn.myth.tomcat.coyote.http11;

import cn.myth.tomcat.util.net.AbstractEndpoint;
import cn.myth.tomcat.util.net.JIoEndpoint;
import cn.myth.tomcat.util.net.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Process Http requests
 */
public class Http11Processor extends AbstractHttp11Processor<Socket> {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    public Http11Processor(JIoEndpoint endpoint) {
        super(endpoint);
    }


    @Override
    protected InputStream getInputStream(SocketWrapper<Socket> socketWrapper, AbstractEndpoint<Socket> endpoint) throws IOException {
        return socketWrapper.getSocket().getInputStream();
    }

    @Override
    protected OutputStream getOutputStream(SocketWrapper<Socket> socketWrapper, AbstractEndpoint<Socket> endpoint) throws IOException {
        return socketWrapper.getSocket().getOutputStream();
    }
}
