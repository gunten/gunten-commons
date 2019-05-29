package gunten.share.aio.server;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

import io.bio.Constant;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
	private CountDownLatch latch;
	/**
	 * @param latch
	 */
	public AcceptHandler(CountDownLatch latch) {
		super();
		this.latch = latch;
	}
	@Override
	public void completed(AsynchronousSocketChannel channel,AsynchronousServerSocketChannel serverChannel) {
		//继续接受其他客户端的请求
		AIOServer.clientCount++;
		System.out.println("连接的客户端数：" + AIOServer.clientCount);
		serverChannel.accept(serverChannel, this);
		
		ByteBuffer buffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);
		//异步读  第三个参数为接收消息回调的业务Handler
		channel.read(buffer, buffer, new ReadHandler(channel,serverChannel, latch));
//		channel.read(buffer).get();
	}
	@Override
	public void failed(Throwable exc, AsynchronousServerSocketChannel serverChannel) {
		System.err.println("srv accept err");
		exc.printStackTrace();
		latch.countDown();
	}
}