package ET;

import java.util.*;
import java.net.*;
import java.io.*;

public class TestServer {

	public static void main(String[] args) {
		for(int i = 0;i < 5;i++)
		{
			Client c = new Client(i+1); //循环生成客户端
			c.doWork();    //客户端向服务器发起请求的操作
		} 
	}
}

class Client{
	Socket sk = null;
	int id;
	public Client(int id) {
		try {
			//设置id号并且连接服务器
			this.id = id;
			this.sk = new Socket("localhost", 8888);
			System.out.println("客户端" + id + "已启动.................");
			//新建收发线程
			Thread recieve = new Thread(new cRecieve(sk,id));
			//开始运行线程
			recieve.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doWork() {
		//开启工作线程
		Thread work = new Thread(new Work(sk));
		work.start();
	}
}

class Work implements Runnable{
	Socket sk = null;
	public Work(Socket sk) {
		this.sk = sk;
	}
	
	public synchronized void run() {
		try {
			String user = "user";
			String pws = "pws";
			int t = 1000;
			//向服务器请求500次密码和500次用户名
			while(t != 0) {
				PrintStream ps = new PrintStream(sk.getOutputStream());
				if(t % 2 == 0)
					ps.println(user);
				else
					ps.println(pws);
				ps.flush();
				t -= 1;
			}
			this.Stop(); //断开连接
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Stop() {//断开连接的函数
		try {
			PrintStream ps = new PrintStream(sk.getOutputStream());
			ps.println('q');  //输入q断开与服务器的连接
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//接收信息线程
class cRecieve implements Runnable{
	private Socket sk;
	private int id;
	public cRecieve(Socket sk,int id) {
		this.sk = sk;
		this.id = id;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(sk.getInputStream(), "GBK"));
			while(true) {
			String message = br.readLine();
			if(message != null)
				System.out.println("客户端" + id + "收到  " + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
