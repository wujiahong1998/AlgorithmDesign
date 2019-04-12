import java.util.*;
import java.net.*;
import java.io.*;
//一个客户端的连接对应一个连接
//多个客户端的连接构成多线程
public class myServer {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try {
				//新建一个服务器套接字,监听的端口为8888
				ServerSocket ss = new ServerSocket(8888);
				//统计客户端的数量
				int count = 0;
				//监听到连接对应客户端的套接字，初始值设置为null
				Socket sk = null;
				System.out.println("服务器已启动，正在等待客户端连接.................");
				while(true) {
					//监听获得一个套接字
					sk = ss.accept();
					//System.out.println("客户端连接成功！");
					//客户端数量加1
					count++;
					//开启对应客户端的线程
					Thread st = new Thread(new ServerThread(sk,count));
					st.start();
					System.out.println("客户端" + count + "连接成功!");	
				}
			} catch (Exception e) {
				e.printStackTrace();
				}
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
