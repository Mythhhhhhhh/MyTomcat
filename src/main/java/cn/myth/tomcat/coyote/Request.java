package cn.myth.tomcat.coyote;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方式 GET/POST
     */
    private String method;

    /**
     * 输入流
     */
    private InputStream inputStream;

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        // 从输入流中获取请求信息
        int count = 0;
        while(count == 0) {
            count = inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String inputStr = new String(bytes);
        // 获取第一行请求头信息
        String firstLineStr = inputStr.split("\\n")[0]; //GET URL / HTTP1.1

        String[] strings = firstLineStr.split(" ");

        this.method = strings[0];
        this.url = strings[1];

        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);
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
}
