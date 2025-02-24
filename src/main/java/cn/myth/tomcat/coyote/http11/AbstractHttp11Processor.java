package cn.myth.tomcat.coyote.http11;


import cn.myth.tomcat.coyote.AbstractProcessor;
import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;
import cn.myth.tomcat.util.net.AbstractEndpoint;
import cn.myth.tomcat.util.net.ExceptionUtils;
import cn.myth.tomcat.util.net.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractHttp11Processor<S> extends AbstractProcessor<S> {

    private static final Logger log = LoggerFactory.getLogger(AbstractHttp11Processor.class);

    public AbstractHttp11Processor(AbstractEndpoint<S> endpoint) {
        super(endpoint);
    }

    @Override
    public void process(SocketWrapper<S> socketWrapper) throws IOException {
        // Process the request in the adapter
        try {
            request = new Request(getInputStream(socketWrapper, endpoint));
            response = new Response(getOutputStream(socketWrapper, endpoint));
            adapter.service(request, response);
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            log.error("Error processing request", t);
            // 500 - Internal Server Error
            // response.setStatus(500);
        }

    }


    protected abstract InputStream getInputStream(SocketWrapper<S> socketWrapper, AbstractEndpoint<S> endpoint) throws IOException;

    protected abstract OutputStream getOutputStream(SocketWrapper<S> socketWrapper, AbstractEndpoint<S> endpoint) throws IOException;

}
