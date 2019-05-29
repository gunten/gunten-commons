/**
 * @author Administrator
 * 2017年3月2日 下午8:51:28
 */
package gunten.share.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 */
public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
	private AsynchronousSocketChannel clientChannel;
	private AsynchronousServerSocketChannel serverChannel;
	private CountDownLatch latch;
	/**
	 * @param channel
	 */
	public WriteHandler(AsynchronousSocketChannel channel,AsynchronousServerSocketChannel serverChannel,
			CountDownLatch latch) {
		this.clientChannel = channel;
		this.serverChannel = serverChannel;
		this.latch = latch;
	}

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		//如果没有发送完，就继续发送直到完成
		if (attachment.hasRemaining())
			clientChannel.write(attachment, attachment, this);
		
//			ByteBuffer readBuffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);
//			//异步读  第三个参数为接收消息回调的业务Handler
//			clientChannel.read(readBuffer, readBuffer, new ReadHandler(clientChannel,serverChannel,latch));
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			System.err.println("srv write err");
			this.clientChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			latch.countDown();
		}
	}

}
