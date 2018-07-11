package server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerTest {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(512 * 1024))
                                    .addLast(new HttpRequestHandler());
                        }
                    });
            ChannelFuture future = sb.bind(8088).sync();
            System.out.println("==========服务已启动==========");
            future.channel().closeFuture().sync();
            System.out.println("==========服务已关闭==========");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            HttpHeaders headers = response.headers();
            boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
            if (keepAlive) {
                headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + ";charset=utf-8");
                headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                headers.add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
//                headers.add(HttpHeaderNames.CONTENT_LENGTH, 11 * 10 + 1);//+1的原因是最后一个分块的10占两个字节
            }
            ctx.writeAndFlush(response);//写响应行和响应头
            for (int i = 1; i <= 10; i++) {
                HttpContent httpContent = new DefaultHttpContent(Unpooled.copiedBuffer("第" + i + "分块 ", CharsetUtil.UTF_8));
                ctx.writeAndFlush(httpContent);//分批次写响应体，每次运行完此行代码，浏览器页面也会显示最新的响应内容
            }
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);//告诉netty结束响应
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("inactive");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("unregister");
        }
    }
}

