package com.yc.http.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.xml.sax.HandlerBase;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.HttpServletRequest;

public class YcHttpServletRequest implements HttpServletRequest{
	private String method; //请求方法
	private String protocal;//协议版本
	private String serverName;//服务器名称
	private int serverPort; //端口
	private String requestURI;//资源的地址
	private String requestURL;//绝对象路径
	private String contextPath;//项目上下文路径
	private String realPath=System.getProperty("user.dir")+File.separatorChar+"webapps";
	//user.dir ->取到当前的class的路径
	
	private String queryString;
	
	private InputStream iis;
	
	public YcHttpServletRequest(InputStream iis){
		this.iis=iis;
		parse();
	}
	
	@Override
	public void parse() {
		String requestInfoString =readFromInputStream();//从输入流中读取请求头
		if(requestInfoString==null||"".equals(requestInfoString)){
			return;
		}
		parseRequestInfoString(requestInfoString);
	}

	private void parseRequestInfoString(String requestInfoString) {
		StringTokenizer st=new StringTokenizer(requestInfoString);
		//GET /xxx/xxx.jpg?sdasdasdas  HTTP/1.1
		if(st.hasMoreTokens()){
			this.method=st.nextToken();
			this.requestURI=st.nextToken();
			this.protocal=st.nextToken();
			this.contextPath="/"+this.requestURI.split("/")[1];//应用上下文路径 wowotuan
		}
		//TODO:后面的请求 键值对  请求实体  暂时不管  再加
		parseParameter(requestInfoString);
	}

	private void parseParameter(String protocal) {
		//1 判断是否有? 有责要取?前面当作requestURI
		//一下解决了地址栏后面的参数解析的问题
		if(requestURI.indexOf("?")>=0){
			this.queryString=requestURI.substring(requestURI.indexOf("?")+1);
			this.requestURI=requestURI.substring(0,requestURI.indexOf("?"));
			//从queruString 中解析出参数 ?name=a&pwd=a
			String[] ss=this.queryString.split("&");
			if(ss!=null&&ss.length>0){
				for(String s:ss){
					String[] paire=s.split("=");
					if(paire!=null&&paire.length>0){
						this.parameters.put(paire[0], paire[1]);
					}
				}
			}
		}
		if(this.method.equals("POST")){
			//post实体中的参数
			//System.out.println(protocal);
			String ps=protocal.substring(protocal.indexOf("\r\n\r\n")+4);
			System.out.println(ps);
			if(ps!=null&&ps.length()>0){
				String[] ss=ps.split("&");
				if(ss!=null&&ss.length>0){
					for(String s:ss){
						System.out.println("sssss"+s);
						String []paire=s.split("=");
						System.out.println("paire 数组长度"+paire.length);
						this.parameters.put(paire[0], paire[1]);
					}
					
				}
			}
		}
	}

	//从输入流中取数据
	private String readFromInputStream() {
		//1.从Input中读出所有的内容 (http请求协议=>protocal)
		String protocal=null;
		//TODO:从流中取Protocol
		StringBuffer sb=new StringBuffer(1024*10); //10k
		int length=-1;
		byte[] bs=new byte[1024*10];
		try {
			length=this.iis.read(bs);
		} catch (IOException e) {

			e.printStackTrace();
			length=-1;
		}
		for(int j=0;j<length;j++){
			sb.append((char)bs[j]);
		}
		
		protocal=sb.toString();
		return protocal;
	}

	public String getMethod() {
		return method;
	}

	public String getProtocal() {
		return protocal;
	}

	public String getServerName() {
		return serverName;
	}

	public String getRealPath() {
		return realPath;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public String getContextPath() {
		return contextPath;
	}

	//父接口中的参数  Object request.getAttribute("")
	private Map<String,Object> attributes=new HashMap<String,Object>();
	@Override
	public Object getAttribute(String key) {
		
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	//请求参数String request.getParameter("")
	private Map<String,String> parameters=new HashMap<String,String>();
	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public Map<String, String> getParameterMap() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	@Override
	public ServletContext getServletContext() {
		
		return YcServletContext.getInstance();
	}

}
