package cn.myth.tomcat.util;

import cn.myth.tomcat.catalina.*;
import cn.myth.tomcat.catalina.connector.Connector;
import cn.myth.tomcat.catalina.connector.CoyoteAdapter;
import cn.myth.tomcat.catalina.core.*;
import cn.myth.tomcat.catalina.loader.WebappClassLoader;
import cn.myth.tomcat.catalina.startup.Catalina;
import cn.myth.tomcat.coyote.Adapter;
import cn.myth.tomcat.coyote.http11.Http11Protocol;
import cn.myth.tomcat.util.net.JIoEndpoint;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Digester解析器
 * 处理XML server.xml
 */
public class Digester {

    private Catalina root;

    public void push(Catalina catalina) {
        this.root = catalina;
    }

    public Catalina parse(FileInputStream inputStream) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document serverDocument = saxReader.read(inputStream);
        Element rootElement = serverDocument.getRootElement();
        Server server = createServer();
        // 初始化Service
        List<Element> servicesList = rootElement.selectNodes("//Service");
        Service[] services = new Service[servicesList.size()];
        for (int i = 0; i < servicesList.size(); i++) {
            services[i] = createService(server, servicesList.get(i));
            server.addService(services[i]);
        }
        root.setServer(server);
        return root;
    }


    /**
     * 创建Service节点
     */
    private Service createService(Server server, Element element) throws DocumentException, IOException {
        List<Element> listConnector = element.selectNodes("//Connector");
        List<Element> listEngine = element.selectNodes("//Engine");
        StandardService service = new StandardService();
        for (int i = 0; i < listConnector.size(); i++) {
            Engine engine = createEngine(service, listEngine.get(i));
            service.setContainer(engine);
        }
        service.setServer(server);

        for (int i = 0; i < listConnector.size(); i++) {
            Element elementConnector = listConnector.get(i);
            String port = elementConnector.attributeValue("port");

            Http11Protocol http11Protocol = new Http11Protocol();
            Connector connector = new Connector(http11Protocol);
            connector.setPort(Integer.parseInt(port));
            connector.setService(service);

            service.addConnectors(connector);
        }
        return service;
    }

    /**
     * 构建Server
     */
    private Server createServer() {
        return new StandardServer();
    }

    /**
     * 创建Engine容器
     */
    private Engine createEngine(Service service, Element element) throws DocumentException, IOException {
        List<Element> listHost = element.selectNodes("//Host");
        Engine engine = new StandardEngine();
        Host[] hosts = new Host[listHost.size()];
        for (int i = 0; i < listHost.size(); i++) {
            hosts[i] = createHost(engine, listHost.get(i));
        }
        engine.setHosts(hosts);
        return engine;
    }

    /**
     * 创建Host容器
     */
    private Host createHost(Engine engine, Element element) throws IOException, DocumentException {
        Host host = new StandardHost();
        String name = element.attributeValue("name");
        String appBase = element.attributeValue("appBase");
        host.setName(name);
        host.setAppBase(appBase);
        host.setEngine(engine);

        File baseFile = new File(host.getAppBase());
        File[] files = baseFile.listFiles();
        for (File web : files) {
            if (web.isDirectory()) {
                // 初始化Context
                Context context = createContext(web);
                host.addContext(context);
            }
        }
        return host;
    }

    /**
     * 创建上下文容器
     */
    private Context createContext(File web) throws IOException, DocumentException {
        Context context = new StandardContext();
        String name = web.getName();
        context.setName(name);
        File classPath = new File(web, "WEB-INF/classes");
        URL[] urls = new URL[1];
        urls[0] = classPath.toURI().toURL();
        context.setClassloader(new WebappClassLoader(urls));
        // 加载web.xml，并解析出Wrapper
        File webXml = new File(web,"/WEB-INF/web.xml");
        if (webXml.exists()) {
            createWrapper(context, webXml);
        }
        return context;
    }


    /**
     * 创建Wrapper servlet包装
     */
    private void createWrapper(Context context, File webXml) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(webXml);
        SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> selectNodes = rootElement.selectNodes("//servlet");

        for (Element element : selectNodes) {
            Element servletNameEle = (Element) element.selectSingleNode("//servlet-name");
            String servletName = servletNameEle.getStringValue();
            Element servletClsEle = (Element) element.selectSingleNode("//servlet-class");
            String servletClass = servletClsEle.getStringValue();

            // 根据servlet-name的值找到url-pattern
            Element servletMappingElement = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
            String urlPattern = servletMappingElement.selectSingleNode("url-pattern").getStringValue();

            Wrapper wrapper = new StandardWrapper();
            wrapper.setServletClass(servletClass);
            wrapper.setName(servletName);
            wrapper.setUrlPattern(urlPattern);
            wrapper.setContext(context);

            context.addWrapper(wrapper);
        }

    }


}
