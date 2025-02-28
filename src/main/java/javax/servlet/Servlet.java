package javax.servlet;

import cn.myth.tomcat.coyote.Request;
import cn.myth.tomcat.coyote.Response;

public interface Servlet {

    /**
     * 初始化
     */
    void init();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 服务
     * 实际应该是 ServletRequest和ServletResponse
     * 这里简化处理
     * @param request 请求
     * @param response 响应
     */
    void service(Request request, Response response);
}
