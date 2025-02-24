package cn.myth.tomcat.coyote;

import cn.myth.tomcat.util.net.AbstractEndpoint;
import cn.myth.tomcat.util.net.SocketWrapper;

import java.io.IOException;

/**
 * 提供所有支持的协议所共有的功能和属性（Tomcat源码里支持HTTP和AJP）
 * 这里只支持HTTP
 */
public abstract class AbstractProcessor<S> implements Processor<S> {

    protected Adapter adapter;
    protected final AbstractEndpoint<S> endpoint;
    protected Request request;
    protected Response response;

    public AbstractProcessor(AbstractEndpoint<S> endpoint) {
        this.endpoint = endpoint;
    }


    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    public abstract void process(SocketWrapper<S> socketWrapper) throws IOException;

}
