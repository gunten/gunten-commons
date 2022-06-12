package netty.bytebufexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * @author gunten
 * 2022/6/12
 */
public class ByteBufLog {

    static void log(ByteBuf buf) {
        StringBuilder sb=new StringBuilder();
        sb.append(" read index:").append(buf.readerIndex());  //读索引
        sb.append(" write index:").append(buf.writerIndex()); //写索引
        sb.append(" capacity :").append(buf.capacity()) ; //容量
        //把ByteBuf中的内容，dump到StringBuilder中
        ByteBufUtil.appendPrettyHexDump(sb,buf);
        System.out.println(sb);
    }
}
