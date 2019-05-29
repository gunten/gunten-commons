package gunten.share.aio.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.bio.Constant;

public class AIOClient {
	private AsyncClientHandler		clientHandle;
	private CountDownLatch			latch;

	/**
	 * @param latch
	 */
	public AIOClient(CountDownLatch latch) {
		super();
		this.latch = latch;
	}
	
	public void start() {
		start(Constant.SERVER_IP, Constant.PORT);
	}

	public  synchronized void start(String ip,int port){
		if(clientHandle!=null)
			return;
		clientHandle = new AsyncClientHandler(ip,port,latch);
//		new Thread(clientHandle,"Client").start();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		CountDownLatch			cdLatch;
		ExecutorService	pool	= Executors.newFixedThreadPool(50);
		List<AIOClient> clients = new ArrayList<AIOClient>();
		cdLatch = new CountDownLatch(Constant.CLIENT_NUM);
		for (int i = 0; i < Constant.CLIENT_NUM; ++i) {
			clients.add(new AIOClient(cdLatch));
		}

		for (AIOClient client : clients) {
			client.start();
			pool.execute(client.clientHandle);
		}
		cdLatch.await();
		pool.shutdown();
	}
}