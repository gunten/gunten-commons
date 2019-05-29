package io.bio;

import java.io.IOException;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BIO服务端源码__伪异步I/O 
 * @version 1.0 
 */  
public final class BIOServer {  

    private static ServerSocket serverSocket;
    //线程池 懒汉式的单例  
    private static ExecutorService executorService = Executors.newFixedThreadPool(60,new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"BIOServer-thread-"+threadNumber.getAndIncrement());
        }
    });

    
    public static void start() throws IOException{  
        start(Constant.PORT);  
    }  
    //这个方法不会被大量并发访问，不太需要考虑效率，直接进行方法同步就行了  
    public synchronized static void start(int port) throws IOException{  
        if(serverSocket != null) return;

        try{  
            //通过构造函数创建ServerSocket  
            //如果端口合法且空闲，服务端就监听成功  
            serverSocket = new ServerSocket(port);
            System.out.println("服务端已启动，端口号：" + port);
            //如果没有客户端接入，将阻塞在accept操作上。
            while(true){
                Socket socket = serverSocket.accept();
                //创建一个新的线程处理这条Socket链路  
                executorService.execute(new ServerHandler(socket));  
            }  
            
        }finally{  
            if(serverSocket != null){
                System.out.println("服务端已关闭。");
                serverSocket.close();
                serverSocket = null;
            }  
        }  
    }  
    
	public static void main(String[] args) {  
	    	
	        try {
				BIOServer.start();
			} catch (IOException e) {
				e.printStackTrace();
			}  
	}
}  
