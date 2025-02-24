package cn.myth.tomcat.catalina.core;

import cn.myth.tomcat.catalina.Container;
import cn.myth.tomcat.catalina.startup.LifecycleException;
import cn.myth.tomcat.catalina.util.LifecycleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class ContainerBase extends LifecycleBase implements Container {

    private static final Logger log = LoggerFactory.getLogger(ContainerBase.class);

    // ----------------------------------------------------- Instance Variables

    /**
     * 该容器的子容器
     * The child Containers belonging to this Container, keyed by name.
     */
    protected final HashMap<String, Container> children = new HashMap<>();


    /**
     * 该容器的名称
     * The human-readable name of this Container.
     */
    protected String name = null;

    /**
     * 该容器父容器
     */
    protected Container parent = null;


    // ------------------------------------------------------------- Properties

    @Override
    public void addChild(Container child) {
        // 源码这里有关于安全的检查判断，Globals.IS_SECURITY_ENABLED，这里省略
        addChildInternal(child);
    }

    private void addChildInternal(Container child) {
        if (log.isDebugEnabled()) {
            log.debug("Add child " + child + " " + this);
        }

        synchronized (children) {
            // 子容器name是否重复
            if (children.get(child.getName()) != null) {
                throw new IllegalArgumentException("addChild:  Child name '" + child.getName() + "' is not unique");
            }
            // 设置该子容器的父容器
            child.setParent(this); // May throw IAE
            // 添加到向子容器Map中
            children.put(child.getName(), child);
        }

        // Start child // 注意下这里，没有将start方法放到synchronized的原因
        // Don't do this inside sync block - start can be a slow process and
        // locking the children object can cause problems elsewhere
        try {
            child.start();
        } catch (LifecycleException e) {
            log.error("Exception starting child " + child, e);
            throw new IllegalStateException("ContainerBase.addChild: start: " + e);
        }
    }

    @Override
    public void removeChild(Container child) {
        // 子容器有生命周期，所以应该是先停止，然后销毁, 最后将children中子容器删除
        if (child == null) {
            return;
        }

        try {
            child.stop();
        } catch (LifecycleException e) {
            log.error("ContainerBase.removeChild: stop: ", e);
        }

        try {
            child.destroy();
        } catch (LifecycleException e) {
            log.error("ContainerBase.removeChild: destroy: ", e);
        }

        synchronized (children) {
            if (children.get(child.getName()) == null) {
                return;
            }
            children.remove(child.getName());
        }

    }


    @Override
    public Container findChild(String name) {
        if (name == null) {
            return null;
        }
        synchronized (children) {
            return children.get(name);
        }
    }

    @Override
    public Container[] findChildren() {
        synchronized (children) {
            Container[] results = new Container[children.size()];
            return children.values().toArray(results);
        }
    }


    @Override
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Container name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Container getParent() {
        return parent;
    }

    @Override
    public void setParent(Container container) {
        this.parent = container;
    }
}
