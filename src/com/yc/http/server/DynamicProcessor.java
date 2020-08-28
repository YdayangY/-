package com.yc.http.server;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

public class DynamicProcessor implements Processor {
	
	@Override  //request的 requestURI=> /res/Hello.do
	public void process(ServletRequest request, ServletResponse response) {
	try {
		//1 取出uri  从uri中取出请求的servlet名字
		String uri=((HttpServletRequest)request).getRequestURI();
		String servletName=uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf("."));//Hello
		Servlet servlet=null;
		//从application中判断是否有这个ServletName
		ServletContext application=YcServletContext.getInstance();//单例
		if(application.getServlet(servletName)!=null){
			servlet=application.getServlet(servletName);
		}else{
			//2 动态字节码加载  到res/ 找servlet.class 文件
			//URLClassloader
			URL[] urls=new URL[1];
			// file://d:\workspace\kittyserver\ HelloServlet.class
			urls[0]=new URL("file",null,YcConstants.KITTYSERVER_BASEPATH);
			//System.out.println(urls[0]);
			URLClassLoader ucl=new URLClassLoader(urls);//classLoader类加载器会自动扫描 urls数组中指定的路径看是否有class
			//URL地址 =》 file:\\\
			//4. Class urlclassLoader.loadClass(类的名字)
			Class c=ucl.loadClass(servletName);  //加载指定的servletName的class字节码
			
			//5.  以反射的形式  newInstance()创建 sevlet实例
			//Servlet servlet=(Servlet)c.newInstance() //-->调用构造方法
			Object o=c.newInstance();
			//6. 再以生命周期的方式来调用servlet中的各方法
			if(o!=null && o instanceof Servlet){
				
				servlet=(Servlet)o;
				application.setServlet(servletName, servlet);//存这个servlet到容器中
				//生命周期调用方法
				servlet.init();
			}
		}
			if(servlet!=null && servlet instanceof Servlet)
			{
				//父类引用只能调用子类重写了父类的方法而不能调用子类所特有的方法
				((HttpServlet)servlet).service((HttpServletRequest)request, (HttpServletResponse)response);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			String bodyentity=e.toString();
			String protocal=gen500(bodyentity.getBytes().length);
			PrintWriter pw=response.getWriter();
			pw.println(protocal);
			pw.println(bodyentity);
			pw.flush();
		}
	}

	private String gen500(int bodylength) {
		String protocal500="HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length:"
				+ bodylength+"\r\n\r\n";
		return protocal500;
	}

}
