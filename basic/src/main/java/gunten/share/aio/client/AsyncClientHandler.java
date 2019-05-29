package gunten.share.aio.client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>, Runnable {
	private AsynchronousSocketChannel clientChannel;
	private String host;
	private int port;
	private CountDownLatch latch;
	public AsyncClientHandler(String host, int port, CountDownLatch latch) {
		this.host = host;
		this.port = port;
		this.latch = latch;
		try {
			//创建异步的客户端通道
			clientChannel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法
		clientChannel.connect(new InetSocketAddress(host, port), this, this);
	}
	//连接服务器成功
	@Override
	public void completed(Void result, AsyncClientHandler attachment) {
		System.out.println("客户端成功连接到服务器...");
		sendMsg(Thread.currentThread().getName()+ " send a request!");
	}
	//连接服务器失败
	@Override
	public void failed(Throwable exc, AsyncClientHandler attachment) {
		System.err.println("连接服务器失败...");
		exc.printStackTrace();
		try {
			clientChannel.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//向服务器发送消息
	public void sendMsg(String msg){
		byte[] req = msg.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		//异步写
		clientChannel.write(writeBuffer, writeBuffer,new WriteHandler(clientChannel, latch));
	}
}