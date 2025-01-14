package cn.myth.tomcat.coyote;

import cn.myth.tomcat.util.HttpProtocolUtil;
import cn.myth.tomcat.util.StaticResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

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

}
