package io.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;

import io.bio.Constant;

/**
 * NIO客户端
 * 
 */
public class NIOClient implements Runnable {

	private final ExecutorService pool =
			Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


	private InetSocketAddress remoteAddress;
	/** 用于主线程等待一次selector中所有任务都完成**/
	private CountDownLatch latch;


	public NIOClient(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	/**
	 * 因为channel的select不能被并发的调用，因此selector私有化，多线程模拟多个Client
	 */
	@Override
	public void run() {
		Selector selector;

		try {
			// 获得一个Socket通道
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			selector = Selector.open();

			//写的时候通过List 批量写，但其实没什么意义
			socketChannel.register(selector, SelectionKey.OP_CONNECT, new ArrayList<String>());
			socketChannel.connect(remoteAddress);

			handleKeys(selector);
		}catch (ClosedChannelException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleKeys(Selector selector) {

		while(!Thread.currentThread().isInterrupted()) {
			try {

				int selectNums = selector.select();
				if (selectNums == 0) {
					System.out.println("client selector 0 continue...");
					continue;
				}

				latch = new CountDownLatch(selectNums);
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if (!key.isValid()) {
						System.out.println("client handleKeys key's not valid...continue");
						key.cancel();
						latch.countDown();
						continue;
					}

                    dispatchKey(key);
				}
				if (latch.getCount() != 0) {
					latch.await();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(Thread.currentThread().getName() + " encounter a connect error");
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(Thread.currentThread().getName() + " is interrupted");
			}
		}

	}

	private void dispatchKey(SelectionKey key) {

		if (key.isConnectable()) {
			AcceptHandler.handle(key, latch);
		} else if (key.isReadable() || key.isWritable() ) {
			pool.execute(new IOHandler(key, latch));
		}
	}
	

	public static void main(String[] args) throws InterruptedException {
		InetSocketAddress remoteAddress = new InetSocketAddress(Constant.SERVER_IP, Constant.PORT);

		Thread ta = new Thread(new NIOClient(remoteAddress),"client A");
		Thread tb = new Thread(new NIOClient(remoteAddress),"client B");
		Thread tc = new Thread(new NIOClient(remoteAddress),"client C");
		Thread td = new Thread(new NIOClient(remoteAddress),"client D");

		ta.start();
		tb.start();
		tc.start();

		Thread.sleep(5000);

		/*结束客户端a*/
		ta.interrupt();
		/*开始客户端d*/
		td.start();
		Thread.sleep(2000);
		tb.interrupt();
		tc.interrupt();
		td.interrupt();
	}
}
