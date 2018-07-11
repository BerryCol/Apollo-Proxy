package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args){

        Bootstrap bootstrap = new Bootstrap();

        EventLoopGroup evnet=new NioEventLoopGroup();

        bootstrap.group(evnet).channel(NioSocketChannel.class)
                .handler(new HttpProxyInitializer());

        ChannelFuture future=bootstrap.connect("www.baidu.com",80);
        future.addListener((ChannelFutureListener) listener ->{
           if(future.isSuccess()){
               System.out.println("连接成功");
           }
        });
    }
}
