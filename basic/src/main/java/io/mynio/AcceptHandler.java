package io.mynio;

import java.io.IOException;
import java.nio.channels.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * handle connect & accept  event
 * @author gunten
 */
public class AcceptHandler {


	public static void handle(SelectionKey key,CountDownLatch connectLatch) {
		if( key.isAcceptable()){

			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			try {
				SocketChannel clientChannel = serverChannel.accept();
				clientChannel.configureBlocking(false);
				System.out.println("  acceptable from "+clientChannel.getRemoteAddress());

				clientChannel.register(key.selector(), SelectionKey.OP_READ, new CopyOnWriteArrayList());
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
				connectLatch.countDown();
			}
		}else if(key.isConnectable()){

			SocketChannel clientChannel = (SocketChannel) key.channel();
			// 完成连接
			if (!clientChannel.isConnectionPending()) {
				connectLatch.countDown();
				return;
			}

			try {
				clientChannel.finishConnect();// finishConnect()方法会阻塞到链接结束并返回是否成功
				System.out.println(Thread.currentThread().getName() +" 连接完成");
				CopyOnWriteArrayList<String> sendQueue = new CopyOnWriteArrayList();
				sendQueue.add(Thread.currentThread().getName() + " send msg");
				clientChannel.register(key.selector(), SelectionKey.OP_WRITE, sendQueue);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				connectLatch.countDown();
			}
		}
	}

}
