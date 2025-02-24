package cn.myth.tomcat.catalina.startup;

import cn.myth.tomcat.catalina.Server;
import cn.myth.tomcat.util.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Catalina
 * 容器 Container
 */
public class Catalina {

    private static final Logger log = LoggerFactory.getLogger(Catalina.class);

    /**
     * 配置文件的路径
     */
    private String configFile = "conf/server.xml";

    public String getConfigFile() {
        return configFile;
    }

    /**
     * Server服务
     */
    private Server server = null;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }


    private File configFile() {
        // CatalinaBase暂时硬编码
        String catalinaBase = this.getClass().getResource("/").getPath()
                .replace("/target/classes", "") + "source/";
        File file = new File(configFile);
        if (!file.isAbsolute()) {
            file = new File(catalinaBase, configFile);
        }
        return file;
    }

    /**
     * 加载：启动一个新的服务器实例
     */
    public void load() {
        // 创建并执行解析器，解析conf/server.xml文件
        Digester digester = createStartDigester();
        File file = null;
        FileInputStream inputStream = null;
        try {
            file = configFile();
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to load server configuration from {}", file, e);
            }
        }

        if (inputStream == null) {
            if (file == null) {
                log.warn("Unable to load server configuration from {}", getConfigFile());
            } else {
                log.warn("Unable to load server configuration from {}", file.getAbsolutePath());
                if (file.exists() && !file.canRead()) {
                    log.warn("Permissions incorrect, read permission is not allowed on the file.");
                }
            }
        }

        try {
            digester.push(this);
            digester.parse(inputStream);
        } catch (Exception e) {
            log.warn("Catalina.start using " + getConfigFile() + ": " , e);
            return;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        // Start the new server
        try {
            getServer().init();
        } catch (LifecycleException e) {
            log.error("Catalina.start", e);
        }

    }

    public void start() {
        if (getServer() == null) {
            load();
        }

        if (getServer() == null) {
            log.error("Cannot start server. Server instance is not configured.");
            return;
        }

        // Start the new server
        try {
            getServer().start();
        } catch (LifecycleException e) {
            log.error("The required Server component failed to start so Tomcat is unable to start.", e);
            try {
                getServer().destroy();
            } catch (LifecycleException e1) {
                log.debug("destroy() failed for failed Server ", e1);
            }
            return;
        }
    }


    private Digester createStartDigester() {
        Digester digester = new Digester();
        return digester;
    }
}
