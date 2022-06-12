package netty.bytebufexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import static netty.bytebufexample.ByteBufLog.log;


public class CompositeByteBufExample {

    public static void main(String[] args) {
        ByteBuf header= ByteBufAllocator.DEFAULT.buffer();
        header.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf body=ByteBufAllocator.DEFAULT.buffer();
        body.writeBytes(new byte[]{6,7,8,9,10});

        //从逻辑成面构建了一个总的buf数据。
        //第二个零拷贝实现
        CompositeByteBuf compositeByteBuf=Unpooled.compositeBuffer();
        //其中第一个参数是 true, 表示当添加新的 ByteBuf 时, 自动递增 CompositeByteBuf的 writeIndex.
        //默认是false，也就是writeIndex=0，这样的话我们不可能从compositeByteBuf中读取到数据。
        compositeByteBuf.addComponents(true,header,body);
        log(compositeByteBuf);

        //Unpooled
        ByteBuf total=Unpooled.wrappedBuffer(header,body);
        log(total);
        header.setByte(2,9);
        log(total);

        //copiedBuffer
        System.out.println("------------copiedBuffer-------------");
        ByteBuf total2=Unpooled.copiedBuffer(header,body);
        log(total2);
        header.setByte(3,'A');
        log(total2);
        log(header);

    }
}
