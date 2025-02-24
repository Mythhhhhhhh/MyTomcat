package cn.myth.tomcat.coyote;

import cn.myth.tomcat.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);
    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方式 GET/POST
     */
    private String method;

    /**
     * 请求的主机名称
     */
    private String host;

    /**
     * 请求上下文名称
     */
    private String contextName;


    private Context context;

    /**
     * 输入流
     */
    private InputStream inputStream;

    public Request() {
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        // 从输入流中获取请求信息
        int count = 0;
        while(count == 0) {
            count = this.inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        //POST /test2/test HTTP/1.1
        //User-Agent: PostmanRuntime/7.43.0
        //Accept: */*
        //Cache-Control: no-cache
        //Postman-Token: 3fda7829-721d-4ba1-8740-7d712c69062b
        //Host: localhost:8080
        //Accept-Encoding: gzip, deflate, br
        //Connection: keep-alive
        //Content-Length: 0

        //GET /test1/test HTTP/1.1
        //Host: localhost:8080
        //Connection: keep-alive
        //sec-ch-ua: "Not(A:Brand";v="99", "Google Chrome";v="133", "Chromium";v="133"
        //sec-ch-ua-mobile: ?0
        //sec-ch-ua-platform: "macOS"
        //Upgrade-Insecure-Requests: 1
        //User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36
        //Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
        //Sec-Fetch-Site: none
        //Sec-Fetch-Mode: navigate
        //Sec-Fetch-User: ?1
        //Sec-Fetch-Dest: document
        //Accept-Encoding: gzip, deflate, br, zstd
        //Accept-Language: zh-CN,zh;q=0.9
        String inputStr = new String(bytes);
        //System.out.println(inputStr);

        // 获取第一行 请求行信息
        String[] split = inputStr.split("\\n");
        String firstLineStr = split[0]; //GET URL / HTTP1.1
        String[] firstLineStrings = firstLineStr.split(" ");
        this.method = firstLineStrings[0];
        this.url = firstLineStrings[1];
        // 获取请求头信息 Host
        for (int i = 1 ; i < split.length ; i++) {
            String requestHeaderLineStr = split[i];
            if (requestHeaderLineStr.startsWith("Host:")) {
                String[] lineStrings = requestHeaderLineStr.split(" ");
                this.host = lineStrings[1].split(":")[0];
            }
        }

        System.out.println("=====>>method:[" + method + "];url:[" + url + "];host:[" + host + "]");
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
