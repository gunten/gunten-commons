package io.bio;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阻塞式I/O创建的客户端 
 * @version 1.0 
 */  
public class BIOClient implements Runnable{  
	private static ExecutorService pool = Executors.newFixedThreadPool(50);


    public static void send(String expression){
        send(Constant.PORT,expression);  
    }

    public static void send(int port,String expression){
        System.out.println("CLIENT 发送：" + expression);  
        Socket socket = null;  
        BufferedReader in = null;  
        PrintWriter out = null;

        try{  
            socket = new Socket(Constant.SERVER_IP,port);  
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            out = new PrintWriter(socket.getOutputStream(),true);  
            out.println(expression);  
            System.out.println("CLIENT 收到：" + in.readLine());  
        }catch(Exception e){
            e.printStackTrace();  
        }finally{  
            if(out != null){  
                out.close();  
                out = null;  
            }
            if(in != null){  
                try {  
                    in.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }finally {
                    in = null;
                }
            }
            if(socket != null){  
                try {  
                    socket.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }finally {
                    socket = null;
                }
            }
        }  
    }  
    
	@Override
	public void run() {
		send(Thread.currentThread().getName() + " send a request");
	}

    public static void main(String[] args) {

        List<BIOClient> clients = new ArrayList(Constant.CLIENT_NUM);
        for (int i = 0; i < Constant.CLIENT_NUM; ++i) {
            clients.add(new BIOClient());
        }
        for(BIOClient client:clients){
            pool.execute(client);
        }
        pool.shutdown();

    }
}  
