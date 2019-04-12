package ET;
import java.util.concurrent.*;  //线程池所在包
import java.util.*;
import java.net.*;
import java.io.*;

/*
 * 使用线程池步骤：
 * 
 * 1.创建线程池
 * 
 * 2.创建任务
 * 
 * 3.执行任务
 * 
 * 4.关闭线程池
 * 
 */
public class ExecuterServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService pool = Executors.newFixedThreadPool(20);  //生成一个线程数为20的线程池
		try {
			ServerSocket ss = new ServerSocket(8888);      //生成服务器套接字
			int count = 0;
			Socket sk = null;
			System.out.println("服务器已启动，正在等待客户端连接.................");
			while(true) {
				sk = ss.accept();
				count++;
				System.out.println("客户端" + count + "连接成功!");
				pool.submit(new ServerThread(sk, count));     //将新建任务加入线程池中
				if(count == 100) break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pool.shutdown();    //关闭线程池
	}

}

class ServerThread implements Runnable{
	private Socket sk;
	private int index;
	public ServerThread(Socket sk,int index) {
		 this.sk = sk;
		 this.index = index;
	}
	@Override
	public void run() {
		PrintStream ps = null;
		BufferedReader br = null;
		String user = "Victor";        //账号
		String psw = "1357924680";     //密码
		try {
			ps = new PrintStream(sk.getOutputStream());
			br = new BufferedReader(new InputStreamReader(sk.getInputStream(), "GBK"));
			
			String message = null;   //收到客户端的消息
			String rmessage = null;  //发送给客户端的消息
			//服务器提示信息
			ps.println("你是第"+index+"个客户端....输入关键字'user'或者'pws',你将得到用户名或密码!断开连接请输入'q'!");
			
			//不断接受客户端的关键字请求，并且做出响应
			while((message = br.readLine()) != null) {
				if(message.equals("user")) {
					System.out.println("客户端"+index+"正在请求获取用户名..........");
					rmessage = "用户名是:" + user;
					ps.println("服务器说:" + rmessage);
				}
				else if(message.equals("pws")) {
					rmessage = "密码是:" + psw;
					System.out.println("客户端"+index+"正在请求获取密码.......... ");
					ps.println("服务器说:" + rmessage);
				}
				else if(message.equals("q"))
				{
					ps.println("......服务结束，感谢您的使用！.......");
					System.out.println("客户端"+index+"已断开连接........");
					break;
				}
				else {
					ps.println("输入错误，请重新输入！");
				}
				
			}
			//关闭输入输出流
			ps.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
