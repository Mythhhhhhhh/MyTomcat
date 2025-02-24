package cn.myth.tomcat.catalina.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Bootstrap {

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * Bootstrap 守护进程对象
     */
    private static volatile Bootstrap daemon = null;

    /**
     * Catalina守护进程对象
     * 一个Catalina实例对象
     */
    private Object catalinaDaemon = null;

    private ClassLoader catalinaLoader = null;

    /**
     * MyTomcat 启动入口
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动MyTomcat
            bootstrap.init();
        } catch (Throwable t) {
            handleThrowable(t);
            t.printStackTrace();
            return;
        }
        daemon = bootstrap;// 当前Bootstrap类对象本身

        try {
            // bootstrap加载
            daemon.load(args);
            // bootstrap启动
            daemon.start();
        } catch (Throwable t) {
            // Unwrap the Exception for clearer error reporting
            if (t instanceof InvocationTargetException &&
                    t.getCause() != null) {
                t = t.getCause();
            }
            handleThrowable(t);
            t.printStackTrace();
            System.exit(1);
        }
    }

    public void init() throws Exception {
        initClassLoaders();

        Class<?> startupClass = catalinaLoader.loadClass("cn.myth.tomcat.catalina.startup.Catalina");
        Object startupInstance = startupClass.getConstructor().newInstance();

        catalinaDaemon = startupInstance; // catalinaDaemon 是一个 Catalina实例
    }

    private void initClassLoaders() {
        catalinaLoader = this.getClass().getClassLoader();
    }

    public void load(String[] arguments) throws Exception {
        // Call the load() method
        String methodName = "load";
        Object[] param = null;
        Class<?>[] paramTypes = null;

        // 通过反射调用Catalina.load()方法
        Method method = catalinaDaemon.getClass().getMethod(methodName, paramTypes);
        log.debug("Calling startup class " + method);
        method.invoke(catalinaDaemon, param);// Catalina.load()
    }

    public void start() throws Exception {
        if (catalinaDaemon == null) {
            init();
        }
        Method method = catalinaDaemon.getClass().getMethod("start", (Class []) null);
        method.invoke(catalinaDaemon, (Object []) null);// Catalina.start()
    }


    // Copied from ExceptionUtils since that class is not visible during start
    private static void handleThrowable(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        }
        // All other instances of Throwable will be silently swallowed
    }
}
