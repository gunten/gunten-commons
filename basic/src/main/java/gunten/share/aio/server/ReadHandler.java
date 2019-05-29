package gunten.share.aio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
	// 用于读取半包消息和发送应答
	private AsynchronousSocketChannel channel;
	private AsynchronousServerSocketChannel serverChannel;
	private CountDownLatch latch;

	public ReadHandler(AsynchronousSocketChannel channel,AsynchronousServerSocketChannel serverChannel,
			CountDownLatch latch) {
		this.channel = channel;
		this.serverChannel = serverChannel;
		this.latch = latch;
	}

	// 读取到消息后的处理
	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		// flip操作
		attachment.flip();

		byte[] message = new byte[attachment.remaining()];
		attachment.get(message);
		try {
			String expression = new String(message, "UTF-8");
			System.out.println("服务器收到消息: " + expression);

			// 向客户端发送消息
			ByteBuffer writeBuffer = ByteBuffer.wrap((Thread.currentThread().getName() + " give a reply").getBytes());
			channel.write(writeBuffer, writeBuffer, new WriteHandler(channel,serverChannel,latch));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			System.err.println("srv read wrong(无视 有客户端退出)");
			exc.printStackTrace();
			this.channel.close();

		} catch (IOException e) {
			System.err.println("close channel err");
			e.printStackTrace();
			latch.countDown();
		}
	}
}