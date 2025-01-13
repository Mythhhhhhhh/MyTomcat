package cn.myth.tomcat.util;

/**
 * HTTP协议工具类
 * 主要是提供响应头信息，这里我们只提供200和404的情况
 */
public class HttpProtocolUtil {


    /**
     * 为响应码200提供请求头信息
     * @param contentLen 内容长度
     */
    public static String getHttp200header(long contentLen) {
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html;charset=utf-8\n" +
                "Content-Length: " + contentLen + "\n" +
                "\r\n";
    }


    /**
     * 为响应码404提供请求头信息(此处也包含了数据内容)
     */
    public static String getHttp404header() {
        String page404 = "<h1>404,Page Not Found.</h1>";
        return "HTTP/1.1 404 Not Found\n" +
                "Content-Type: text/html;charset=utf-8\n" +
                "Content-Length: " + page404.getBytes().length + "\n" +
                "\r\n" + page404;
    }
}
