package cn.myth.tomcat.coyote;

/**
 * Adapter. This represents the entry point in a coyote-based servlet container.
 *
 * @see ProtocolHandler
 */
public interface Adapter {

    void service(Request request, Response response) throws Exception;

}
