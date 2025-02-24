package cn.myth.tomcat.catalina.connector;

import cn.myth.tomcat.catalina.Context;
import cn.myth.tomcat.catalina.Engine;
import cn.myth.tomcat.catalina.Host;
import cn.myth.tomcat.catalina.Wrapper;
import cn.myth.tomcat.coyote.Adapter;
import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CoyoteAdapter implements Adapter {

    private static final Logger log = LoggerFactory.getLogger(CoyoteAdapter.class);

    // The CoyoteConnector with which this processor is associated.
    private final Connector connector;

    public CoyoteAdapter(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        // 查找Context
        Engine engine = connector.getService().getContainer();
        boolean isInHost = false;
        Host[] hosts = engine.getHosts();
        for (Host host : hosts) {
            if (!host.getName().equalsIgnoreCase(request.getHost())) {
                continue;
            }
            Context[] contexts = host.getContexts();
            for (Context context : contexts) {
                String[] split = request.getUrl().split("/");
                if (context.getName().equalsIgnoreCase(split[1])) {
                    // 查找上下文
                    request.setContext(context);
                    response.setContext(context);
                    isInHost = true;
                    response.setHost(host);
                    break;
                }
            }
            if (isInHost) {
                break;
            }
        }

        // 查找servlet并且执行
        if (request.getContext() != null) {
            String contextName = request.getContext().getName();
            String urlPattern = request.getUrl().replaceFirst("/" + contextName, "");
            Wrapper wrapper = request.getContext().findWrapper(urlPattern);
            if (wrapper == null) {
                try {
                    response.outputHtmlAppBase(request.getUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                wrapper.getServlet().service(request,response);
            }
        }
    }

}
