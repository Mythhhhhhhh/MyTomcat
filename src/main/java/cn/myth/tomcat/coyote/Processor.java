package cn.myth.tomcat.coyote;

import cn.myth.tomcat.util.net.SocketWrapper;

import java.io.IOException;

/**
 * Common interface for processors of all protocols.
 */
public interface Processor<S> {

    void process(SocketWrapper<S> socketWrapper) throws IOException;
}
