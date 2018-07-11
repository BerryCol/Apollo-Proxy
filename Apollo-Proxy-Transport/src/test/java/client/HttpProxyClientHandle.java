package client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCountUtil;


public class HttpProxyClientHandle extends ChannelInboundHandlerAdapter {

//  private Channel clientChannel;
//
//  public HttpProxyClientHandle(Channel clientChannel) {
//    this.clientChannel = clientChannel;
//  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //客户端channel已关闭则不转发了
    System.out.println(msg);

  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    ctx.channel().close();

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();

  }
}
