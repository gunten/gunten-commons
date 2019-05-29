package gunten.share.aio.server;

import io.bio.Constant;

/**
 * AIO服务端
 * @version 1.0
 */
public class AIOServer {
	private static AsyncServerHandler serverHandle;
	public volatile static long clientCount = 0;
	public static void start(){
		start(Constant.PORT);
	}
	public static synchronized void start(int port){
		if(serverHandle!=null)
			return;
		serverHandle = new AsyncServerHandler(port);
		new Thread(serverHandle,"Server").start();
	}
	public static void main(String[] args){
		AIOServer.start();
	}
}
