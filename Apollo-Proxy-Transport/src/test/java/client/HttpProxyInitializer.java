package client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.proxy.ProxyHandler;

/**
 * HTTP代理，转发解码后的HTTP报文
 */
public class HttpProxyInitializer extends ChannelInitializer {

//  private Channel clientChannel;
//  private ProxyHandler proxyHandler;
//
//  public HttpProxyInitializer(Channel clientChannel, RequestProto requestProto,
//                              ProxyHandler proxyHandler) {
//    this.clientChannel = clientChannel;
//    this.proxyHandler = proxyHandler;
//  }

  @Override
  protected void initChannel(Channel ch) throws Exception {


    ch.pipeline().addLast("httpCodec", new HttpClientCodec());
    ch.pipeline().addLast("proxyClientHandle", new HttpProxyClientHandle());
  }
}
