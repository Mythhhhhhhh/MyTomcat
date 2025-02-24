package cn.myth.tomcat.util.net;

public class SocketWrapper<E> {

    protected volatile E socket;

    public SocketWrapper(E socket){
        this.socket = socket;
    }

    public E getSocket() {
        return socket;
    }

}
