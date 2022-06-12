package netty.bytebufexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static netty.bytebufexample.ByteBufLog.log;

public class ByteBufCreateExample {

    public static void main(String[] args) {
        //由JVM来管理内存
        ByteBuf buf=ByteBufAllocator.DEFAULT.heapBuffer(); //堆内存
        buf.writeBytes(new byte[]{1,2,3,4});
        log(buf);
        buf.writeInt(5);
        log(buf);
        System.out.println("开始进行读取操作");
        buf.markReaderIndex(); //标记索引位置.  markWriterIndex()
        byte b=buf.readByte();
        System.out.println(b);
        buf.resetReaderIndex(); //重新回到标记位置
        log(buf);
    }



}
