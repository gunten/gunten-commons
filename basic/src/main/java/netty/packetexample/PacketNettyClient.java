package netty.packetexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;

public class PacketNettyClient {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
//                                .addLast(new FixedLengthFrameDecoder(36))
                                //表示传输消息的时候，在消息报文中增加4个字节的length。->发送的ByteBuf
                                .addLast(new LengthFieldPrepender(4,0,false))
                                .addLast(new StringEncoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                        ctx.writeAndFlush("i am first request");
                                        ctx.writeAndFlush("i am second request");
                                        super.channelActive(ctx);
                                    }
                                });
//                                .addLast(new SimpleClientHandler());

                    }
                });
        try {
            ChannelFuture future=bootstrap.connect("localhost",8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
