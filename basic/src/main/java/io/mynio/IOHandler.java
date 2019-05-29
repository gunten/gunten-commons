package io.mynio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import io.bio.Constant;

/**
 *  handle read & write  event
 */
public class IOHandler implements Runnable
{
	public enum RoleEnum{
		SERVER,
		CLIENT
	}

	private SelectionKey key;
	private CountDownLatch ioLatch;

	//给非并发版使用
	public IOHandler() { }

    public IOHandler(SelectionKey key,CountDownLatch latch) {
		this.key = key;
		this.ioLatch = latch;
    }

    @Override
    public void run() {
		try {
			handle(key);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if(ioLatch != null)
				ioLatch.countDown();
		}
	}

	public void handle(SelectionKey key) throws IOException, InterruptedException {
		SocketChannel socketChannel = null;
		
		if (key.isReadable()) {
			socketChannel = (SocketChannel) key.channel();
			RoleEnum role;

			//这步能解决客户端友好关闭通道，强关还是有问题
			if( ( role = innerRead(key)) == null ){
				System.out.println("断开..." + socketChannel.socket().getRemoteSocketAddress());
				key.cancel();
				socketChannel.close();

			}else{
				// 如果是client,先写后读 取消注册，结束通讯
				if( role.equals(RoleEnum.CLIENT)) {
					key.cancel();
					/** 断开与服务端链接**/
					socketChannel.close();
				}else{
				//服务端先读再写
					CopyOnWriteArrayList<String> sendQueue =(CopyOnWriteArrayList<String>) key.attachment();
					sendQueue.add(Thread.currentThread().getName() + " reply END");
					System.out.println("server send " +Thread.currentThread().getName() + " reply END");
//						key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
					socketChannel.register(key.selector(), SelectionKey.OP_WRITE, key.attachment());
				}
			}

		} else if (key.isWritable()) {
			innerWrite("", key);
			key.interestOps(key.interestOps() | SelectionKey.OP_READ);
			//or
			// 注册 Client Socket Channel 到 Selector
//			key.channel().register(key.selector(), SelectionKey.OP_READ);
		}else{
			System.out.println("inCurrect key at IOHandler");
		}
	}


	/**
	 *
	 * @param key
	 * @return
	 */
	private RoleEnum innerRead(SelectionKey key) {
		ByteBuffer buffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);
		String str = null;
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			int count = channel.read(buffer);
			if (count < 0) return null;

			if (buffer.position() > 0) {
				buffer.flip();
				byte[] bytes = new byte[buffer.remaining()];
				/** 一定要用copy **/
				System.arraycopy(buffer.array(), buffer.position(), bytes, 0, buffer.remaining());
				str = new String(bytes, Charset.forName("UTF-8"));
				System.out.println("read from "+ channel.getRemoteAddress().toString() + "：[" + str+"]");
			}
			if(str.endsWith("END"))
				return RoleEnum.CLIENT;
			else
				return RoleEnum.SERVER;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	private void innerWrite(String mes, SelectionKey key) {

		SocketChannel clientSocketChannel = (SocketChannel) key.channel();
		// 遍历响应队列
		CopyOnWriteArrayList<String> responseQueue = (CopyOnWriteArrayList<String>) key.attachment();

		for (String content : responseQueue) {

			ByteBuffer buffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);
			try {
				buffer.put(content.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			// 写入 Channel
			buffer.flip();
			try {
				// 注意，不考虑写入超过 Channel 缓存区上限。
				clientSocketChannel.write(buffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		responseQueue.clear();
	}

}
