package cn.myth.tomcat.coyote;

import cn.myth.tomcat.catalina.Context;
import cn.myth.tomcat.catalina.Host;
import cn.myth.tomcat.util.HttpProtocolUtil;
import cn.myth.tomcat.util.StaticResourceUtil;

import java.io.*;

public class Response {

    /**
     * 主机
     */
    private Host host;

    /**
     * 上下文
     */
    private Context context;

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    // 使用输出流输出指定字符串
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }


    /**
     *
     * @param path 根据url来获取到静态资源的绝对路径，进一步根据绝对路径读取该静态资源文件，
     *             最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源文件的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsoluteResourcePath(path);

        // 输出静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()) {
            // 输出静态资源
            StaticResourceUtil.outputResource(new FileInputStream(file), outputStream);
        } else {
            // 输出404
            output(HttpProtocolUtil.getHttp404header());
        }
    }

    public void outputHtmlAppBase(String path) throws IOException {
        String absolutePath = host.getAppBase() + "/" + path;
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            InputStream fis = new FileInputStream(file);
            StaticResourceUtil.outputResource(fis,outputStream);
        } else {
            output(HttpProtocolUtil.getHttp404header());
        }
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
