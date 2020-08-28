package com.yc.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yc.ThreadPool.ThreadPoolManger;;

public class KittyServer {
	private ThreadPoolManger tpm;//线程池管理器
	
	public static void main(String[] args) throws Exception {
		KittyServer ks=new KittyServer();
		ks.startServer();
	}
	boolean flag=false;
	private void startServer() throws Exception {
		ServerSocket ss=null;
		//TODO:此处修改成解析 xml 为一个map 参数都可以从map中取
		int port=parseServerXml();
		//TODO: 改写成从xml中读取  线程池配置
		tpm=new ThreadPoolManger(10,null);
		try {
			ss=new ServerSocket(port);
			YcConstants.logger.debug("kitty server is starting, and listening to port "+ss.getLocalPort());
		} catch (IOException e) {
			YcConstants.logger.error("kitty server's port "+port+"is alread in use");
			return;
		}
		while(!flag){
			try {
				Socket s=ss.accept();
				YcConstants.logger.debug("a client "+s.getInetAddress()+" is already to kitty server");
				TaskService ts=new TaskService(s);
				//TODO: 判断是否采用了线程池  如果是则用线程池任务
				tpm.process(ts);
				//TODO  不是则采用原生 的Thread
				/**
				 * Thread t=new Threa(ts);
				 * t.start;
				 */
			} catch (Exception e) {
				YcConstants.logger.error("client is down, cause:"+e.getMessage());
			}
		}
	}
	
	/**
	 * TODO 可以改写成解析xml 返回Map<String,Object>的结构  Object 代表可能是String 也可能是map
	 * @return
	 * @throws Exception
	 */
	private int parseServerXml() throws Exception {
		List<Integer> list=new ArrayList<Integer>();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();//通过DocumentBuilderFactory创建xml解析器
		try {
			DocumentBuilder bulider=factory.newDocumentBuilder();//通过解析器创建一个可以加载并生成xml的DocumentBuilder
			Document doc=bulider.parse(YcConstants.SERVERCONFIG);//通过解释器创建一个可以加载并生成XML的DocumentBuilder
			NodeList nl=doc.getElementsByTagName("Connector");//遍历书读取相应节点
			for(int i=0;i<nl.getLength();i++){
				Element node=(Element)nl.item(i);
				list.add(Integer.parseInt(node.getAttribute("port")));
			}
		} catch (Exception e) {
			throw e;
		}
		return list.get(0);
	}
}
