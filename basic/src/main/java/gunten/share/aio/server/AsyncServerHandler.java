package gunten.share.aio.server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
public class AsyncServerHandler implements Runnable {
	public CountDownLatch latch;
	public AsynchronousServerSocketChannel serverChannel;
	public AsyncServerHandler(int port) {
		try {
			//创建服务端通道&绑定端口
			serverChannel = AsynchronousServerSocketChannel.open();
			serverChannel.bind(new InetSocketAddress(port));
			System.out.println("服务器已启动，端口号：" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		//CountDownLatch初始化
		//它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
		//此处并未调用CountDownLatch.countDown()，让现场一直阻塞，防止服务端执行完成后退出
		//生产环境就不需要担心这个问题，因为服务端是不会退出的
		latch = new CountDownLatch(1);
		//用于接收客户端的连接
		serverChannel.accept(serverChannel,new AcceptHandler(latch));
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}