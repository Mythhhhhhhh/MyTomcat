package cn.myth.tomcat.coyote;

import cn.myth.tomcat.util.net.AbstractEndpoint;
import cn.myth.tomcat.util.net.ExceptionUtils;
import cn.myth.tomcat.util.net.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectName;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Executor;

public abstract class AbstractProtocol<S> implements ProtocolHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractProtocol.class);

    // The adapter provides the link between the ProtocolHandler and the connector.
    protected Adapter adapter;
    @Override
    public void setAdapter(Adapter adapter) { this.adapter = adapter; }
    @Override
    public Adapter getAdapter() { return adapter; }


    // 网络I/O端点，必须与ProtocolHandler的实现相匹配
    protected AbstractEndpoint<S> endpoint = null;

    public InetAddress getAddress() { return endpoint.getAddress(); }
    public int getPort() { return endpoint.getPort(); }
    public void setPort(int port) {
        endpoint.setPort(port);
    }
    @Override
    public Executor getExecutor() { return endpoint.getExecutor(); }


    // ------------------------------------------------------- Lifecycle methods
    @Override
    public void init() throws Exception {
        log.info("Initializing ProtocolHandler {}", getName());
        String endpointName = getName();
        endpoint.setName(endpointName.substring(1, endpointName.length()-1));
        try {
            endpoint.init();
        } catch (Exception e) {
            log.error("Failed to initialize end point associated with ProtocolHandler {}", getName(), e);
            throw e;
        }
    }

    // 协议的名称：前缀-地址-端口
    public String getName() {
        StringBuilder name = new StringBuilder(getNamePrefix());
        name.append('-');
        if (getAddress() != null) {
            name.append(getAddress().getHostAddress());
            name.append('-');
        }
        int port = getPort();
        name.append(port);
        return ObjectName.quote(name.toString());
    }

    protected abstract String getNamePrefix();

    @Override
    public void start() throws Exception {
        log.info("Starting ProtocolHandler {}", getName());
        try {
            endpoint.start();
        } catch (Exception e) {
            log.error("Failed to start end point associated with ProtocolHandler {}", getName(), e);
            throw e;
        }
    }

    // ------------------------------------------- Connection handler base class
    protected abstract static class AbstractConnectionHandler<S, P extends Processor<S>>
            implements AbstractEndpoint.Handler {

        //protected abstract AbstractProtocol<S> getProtocol();
        //protected final Map<S,Processor<S>> connections = new ConcurrentHashMap<>();

        public void process(SocketWrapper<S> wrapper) {
            if (wrapper == null) {
                return;
            }
            S socket = wrapper.getSocket();
            if (socket == null) {
                return;
            }
            Processor<S> processor;
            try {
                processor = createProcessor();
                processor.process(wrapper);
            } catch (SocketException e) {
                log.debug("SocketExceptions are normal, ignored", e);
            } catch (IOException e) {
                log.debug("IOExceptions are normal, ignored", e);
            } catch (OutOfMemoryError e) {
                log.error("Failed to complete processing of a request", e);
            } catch (Throwable e) {
                ExceptionUtils.handleThrowable(e);
                log.error("Error reading request, ignored", e);
            }
        }

        protected abstract P createProcessor();
    }





}
