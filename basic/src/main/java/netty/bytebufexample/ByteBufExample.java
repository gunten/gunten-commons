package netty.bytebufexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static netty.bytebufexample.ByteBufLog.log;

public class ByteBufExample {

    public static void main(String[] args) {
        ByteBuf buf=ByteBufAllocator.DEFAULT.buffer(); //可自动扩容
        System.out.println("=======before ======");
        log(buf);
        //构建一个字符串
        StringBuilder stringBuilder=new StringBuilder(); //演示的时候，可以把循环的值扩大，就能看到扩容效
        for (int i = 0; i < 40; i++) {
            stringBuilder.append("-"+i);
        }
        buf.writeBytes(stringBuilder.toString().getBytes());
        System.out.println("=======after ======");
        buf.readShort(); //读取2个字节
        buf.readByte(); //读取一个字节

        log(buf);
    }
}
