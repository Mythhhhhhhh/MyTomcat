package cn.myth.tomcat.coyote.http11;

import cn.myth.tomcat.util.net.JIoEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class Http11Protocol extends AbstractHttp11Protocol<Socket> {

    private static final Logger log = LoggerFactory.getLogger(Http11Protocol.class);

    protected Http11ConnectionHandler cHandler;

    public Http11Protocol() {
        endpoint = new JIoEndpoint();
        cHandler = new Http11ConnectionHandler(this);
        ((JIoEndpoint) endpoint).setHandler(cHandler);
    }

    @Override
    protected String getNamePrefix() {
        return "http-bio";
    }

    // -----------------------------------  Http11ConnectionHandler Inner Class
    protected static class Http11ConnectionHandler extends AbstractConnectionHandler<Socket, Http11Processor>
            implements JIoEndpoint.Handler {

        protected final Http11Protocol proto;

        Http11ConnectionHandler(Http11Protocol proto) {
            this.proto = proto;
        }

        @Override
        protected Http11Processor createProcessor() {
            Http11Processor processor = new Http11Processor((JIoEndpoint) proto.endpoint);
            processor.setAdapter(proto.adapter);
            return processor;
        }
    }

}
