package com.yc.javax.servlet.http;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

public abstract class HttpServlet implements Servlet {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	protected void doGet(HttpServletRequest request,HttpServletResponse response){}
	protected void doPost(HttpServletRequest request,HttpServletResponse response){}
	protected void doHead(HttpServletRequest request,HttpServletResponse response){}
	protected void doDelete(HttpServletRequest request,HttpServletResponse response){}
	
	public void service(HttpServletRequest request, HttpServletResponse response){
		String method=((HttpServletRequest)request).getMethod();
		//判断调用方法
		if("get".equalsIgnoreCase(method)){
			//HttpServlet ->Hello 继承 HttpServlet
			//请求调用 HelloServelt时,在service()-> doGet()
			doGet((HttpServletRequest)request,(HttpServletResponse)response);
		}else if("post".equalsIgnoreCase(method)){
			//HttpServlet ->Hello 继承 HttpServlet
			//请求调用 HelloServelt时,在service()-> doGet()
			doGet((HttpServletRequest)request,(HttpServletResponse)response);
		}else if("head".equalsIgnoreCase(method)){
			//HttpServlet ->Hello 继承 HttpServlet
			//请求调用 HelloServelt时,在service()-> doGet()
			doGet((HttpServletRequest)request,(HttpServletResponse)response);
		}else if("delete".equalsIgnoreCase(method)){
			//HttpServlet ->Hello 继承 HttpServlet
			//请求调用 HelloServelt时,在service()-> doGet()
			doGet((HttpServletRequest)request,(HttpServletResponse)response);
		}
		//调用doGet/doPost
	}
	
	public void service(ServletRequest request, ServletResponse response) {
		this.service((HttpServletRequest)request,(HttpServletResponse) response);
	}
	
}
