package web;

import web.entity.Method;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by snow on 2018/4/21.
 */
public class Request {
    String method;  //请求方法
    String url;    //请求地址
    String version;  //版本
    ArrayList<String> headerLine = new ArrayList<>();  //请求的报文内容

    Hashtable<String, String> postEntityBody = new Hashtable<>();  //post提交的信息
    String putEntityBody = "";   //put上传的信息

    public Request(InputStream is) { //构造函数直接读取信息
        //DataInputStream di = new DataInputStream(is);
        //最好别用BR，很多BUGGGGG!!!!!
        try {
            byte[] buff = new byte[is.available()];
            is.read(buff);  //获取整个请求报文
            String mHeader = new String(buff);
            String[] lines = mHeader.split("\r\n");
            //Request Line
            String first = lines[0];
            parseRequestLine(first);    //解析第一行获取status、版本和url
            int i;
            //Header Line
            for(i = 1;i < lines.length;i++)
            {
                if(lines[i].equals(""))
                    break;
                parseRequestHeaderLine(lines[i]);  //解析报文头
            }
            System.out.println("");
            i++;
            //Entity Body: POST
            if (method.equals(Method.POST)) {
                String message = lines[i];
                System.out.println(message);
                String[] r = message.split("&");
                String[] f =r[0].split("=");
                String[] s = r[1].split("=");
                postEntityBody.put(f[0],f[1]);
                postEntityBody.put(s[0],s[1]);
            }
            //String str = null;
            //Entity Body: PUT
            /*if (method.equals(Method.PUT)) {
                while (str != null) {
                    str = reader.readLine();
                    if(str == null)
                        break;
                    System.out.println(str);

                    //boundary code
                    if (str.contains("-----"))
                        continue;

                    //Content-Disposition
                    if (str.contains("Content-Disposition")) {
                        try {
                            //cr lf
                            str = reader.readLine();
                            System.out.println(str);

                            //entity
                            while (str != null) {
                                str = reader.readLine();
                                System.out.println(str);
                                putEntityBody += str;
                            }
                            continue;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Parse Request Error");
        }
    }

    /**
     * 解析Request Line
     *
     * @param str
     */
    private void parseRequestLine(String str) {
        System.out.println(str);
        String[] split =  str.split("\\s+");
        try {
            method = split[0];
            if (!Method.methods.contains(method)) {
                method = Method.UNRECOGNIZED;
            }
            if(split.length > 1)
            {
                url = split[1];
                version = split[2];
            }
            else {
                url = "/index.html";
                version = "HTTP/1.1";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestLine Error");
        }
    }

    /**
     * 解析Header Line
     *
     * @param str
     */
    private void parseRequestHeaderLine(String str) {
        System.out.println(str);
        headerLine.add(str);  //add
    }

}
