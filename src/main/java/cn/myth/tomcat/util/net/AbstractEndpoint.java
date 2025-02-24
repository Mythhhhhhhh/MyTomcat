package cn.myth.tomcat.util.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.*;

public abstract class AbstractEndpoint<S> {

    private static final Logger log = LoggerFactory.getLogger(AbstractEndpoint.class);

    public static interface Handler {

        //Object getGlobal();

        //void recycle();
    }

    /**
     * Acceptor接受socket，然后从Worker线程池中找出空闲的线程处理socket
     * 如果没有空闲线程，Acceptor将阻塞
     * 若使用BIO，tomcat可以同时处理的socket数目不能超过最大线程数
     */
    public abstract static class Acceptor implements Runnable {

        private String threadName;
        protected final void setThreadName(final String threadName) {
            this.threadName = threadName;
        }
        protected final String getThreadName() {
            return threadName;
        }

    }


    // External Executor based thread pool.
    private Executor executor = null;
    public void setExecutor(Executor executor) { this.executor = executor; }
    public Executor getExecutor() { return executor; }

    public void createExecutor() {
        // 定义一个线程池
        executor = new ThreadPoolExecutor(
                10,
                50,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    // Name of the thread pool, which will be used for naming child threads.
    private String name = "TP";
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    // Server socket port
    private int port;
    public int getPort() { return port;}
    public void setPort(int port) { this.port = port; }

    // Address for the server socket.
    private InetAddress address;
    public InetAddress getAddress() { return address; }
    public void setAddress(InetAddress address) { this.address = address; }

    // Threads used to accept new connections and pass them to worker threads.
    protected Acceptor[] acceptors;

    // Acceptor thread count
    protected int acceptorThreadCount = 1;
    public void setAcceptorThreadCount(int acceptorThreadCount) {
        this.acceptorThreadCount = acceptorThreadCount;
    }
    public int getAcceptorThreadCount() { return acceptorThreadCount; }


    // ------------------------------------------------------- Lifecycle methods

    public abstract void bind() throws Exception;
    public abstract void startInternal() throws Exception;

    public void init() throws Exception {
        log.info("{}初始化中...", this.getClass().getName());
        bind();
        log.info("{}初始化完成.", this.getClass().getName());
    }

    public void start() throws Exception {
        log.info("{}启动中...", this.getClass().getName());
        startInternal();
        log.info("{}启动完成.", this.getClass().getName());
    }

    protected final void startAcceptorThreads() {
        int count = getAcceptorThreadCount();
        acceptors = new Acceptor[count];
        for (int i = 0; i < count; i++) {
            acceptors[i] = createAcceptor();
            String threadName = getName() + "-Acceptor-" + i;
            acceptors[i].setThreadName(threadName);
            Thread t = new Thread(acceptors[i], threadName);
            //t.setPriority(Thread.NORM_PRIORITY);
            //t.setDaemon(true);
            t.start();
        }
    }

    /**
     * Hook to allow Endpoints to provide a specific Acceptor implementation.
     */
    protected abstract Acceptor createAcceptor();


}
