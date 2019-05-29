package io.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import io.bio.Constant;

/**
 * Reactor模式 Server端
 * Created by gunten on 17/2/8.
 */
public class ReactorWithWorkerThreadPool
{
	private final Selector selector;
	private final ServerSocketChannel serverSocketChannel;
	private ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),new ThreadFactory() {
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,"Server-thread-"+threadNumber.getAndIncrement());
		}
	});


	/** 用于主线程等待一次selector中所有任务都完成**/
	private CountDownLatch latch;

	public ReactorWithWorkerThreadPool(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();

		try {
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务端启动完成");

            handleKeys();

		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void handleKeys() {

		while (!Thread.currentThread().isInterrupted()) {

			try {

				int selectNums = selector.select(); // this could block
				if (selectNums == 0) {
					System.out.println("选择 Channel 数量：" + selectNums + " continue...");
					continue;
				}

				latch = new CountDownLatch(selectNums);
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selected.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if (!key.isValid()) {
						System.out.println("server found key's not valid...continue");
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
				try{
					selector.close();
				}catch(IOException e1){
					System.out.println("selector close failed");
				}finally{
					System.out.println("server close");
				}
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " is interrupted");
			}

		}

	}

	

	private void dispatchKey(SelectionKey key) {

		// 主线程负责轮询和 处理accept事件
		if (key.isAcceptable()) {
			AcceptHandler.handle(key, latch);
		}else{
		    //读写/业务事件使用线程池
			pool.execute(new IOHandler(key, latch));

//			单线程版
//			new IOHandler().handle(key);
		}
	}


	public static void main(String args[]) {
			try {
				new ReactorWithWorkerThreadPool(Constant.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
