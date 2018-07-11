package org.apollo.channel;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.kqueue.KQueue;

public final class NativeSupport {

    /**
     * 检测是否是可以使用epoll的linux系统，netty利用了JNI，直接使用Native方法
     * @return
     */
    public static boolean isNativeEpollAvaliable(){
        return Epoll.isAvailable();
    }

    /**
     * 检测是否可以使用KQueue的BSD系统，netty利用了JNI，直接使用Native方法
     * @return
     */
    public static boolean isNativeKQueueAvailable(){
        return KQueue.isAvailable();
    }
}
