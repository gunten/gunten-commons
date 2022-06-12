package netty.packetexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        System.out.println("客户端连接建立成功");
        for (int i = 0; i < 10; i++) {
            ByteBuf buf= Unpooled.copiedBuffer("客户端消息:"+i, Charset.forName("utf-8"));
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("收到服务端返回的消息");
        ByteBuf buf=(ByteBuf)msg;
        byte[] data=new byte[buf.readableBytes()];
        buf.readBytes(data);
        String message=new String(data,Charset.forName("utf-8"));
        System.out.println("收到服务端的消息内容:"+message);
        System.out.println("客户端收到的消息数量："+(++count));

        super.channelRead(ctx, msg);
    }
}
