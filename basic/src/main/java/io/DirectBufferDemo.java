package io;

import java.nio.ByteBuffer;

/**
 * @author gunten
 * 2019/5/13
 */
public class DirectBufferDemo {
    public static void main(String[] args) {

        //这两个都是protect 类，不能直接
        //    HeapByteBuffer hbb;
        //    DirectByteBuffer directByteBuffer;

        //分配一块1024Bytes的堆外内存(直接内存)
        //allocateDirect方法内部调用的是DirectByteBuffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.capacity());        //向堆外内存中读写数据
        buffer.putInt(0, 2018);
        buffer.putInt(2019);
        System.out.println(buffer.getInt(0));
        System.out.println("limit:"+buffer.limit());


        int max = 1000;
        Object[] arr = new Object[max];
        heapCheck();
        for (int n=0; n<max; n++) {
            arr[n] = java.nio.ByteBuffer.allocateDirect(10*1024*1024);
            System.out.println((n+1)+": "
                    +((n+1)*10)+" MB of objects created.");
            heapCheck();
        }
    }

    public static void heapCheck() {
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        long used = total - free;
        java.io.Console con = System.console();
        con.format("Total memory: %s%n",total);
        con.format(" Free memory: %s%n",free);
        con.format(" Used memory: %s%n",used);
        String str = con.readLine("Press ENTER key to continue: ");
    }
}
