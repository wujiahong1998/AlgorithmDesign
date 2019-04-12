import java.util.*;
import java.net.*;
import java.io.*;

public class myClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//连接服务器
			Socket sk = new Socket("localhost", 8888);
			System.out.println("客户端已启动.................");
			
			//新建收发线程
			Thread recieve = new Thread(new cRecieve(sk));
			Thread send = new Thread(new cSend(sk));
			
			//开始运行线程
			recieve.start();
			send.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class cSend implements Runnable{
	private Socket sk;
	//传入Socket初始化
	public cSend(Socket sk) {
		this.sk = sk;
	}
	@Override
	public void run() {
		try {
			Scanner in = new Scanner(System.in);
			while(true) {
				PrintStream ps = new PrintStream(sk.getOutputStream());
				//当输入时获取信息
				String message = in.nextLine();
				//写入服务器，让服务器读取
				ps.println(message);
				ps.flush();
				if(message.equals("q")) System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class cRecieve implements Runnable{
	private Socket sk;
	public cRecieve(Socket sk) {
		this.sk = sk;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(sk.getInputStream(), "GBK"));
			while(true) {
			String message = br.readLine();
			if(message != null)
				System.out.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
