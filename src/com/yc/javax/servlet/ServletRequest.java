package com.yc.javax.servlet;

import java.util.Map;

public interface ServletRequest {
	public String getRealPath();
	public Object getAttribute(String key);
	public void setAttribute(String key,Object value);
	
	/**
	 * 获取  通过 http://localhost:8083/xxx/xxx.jsp?name=a&age=3
	 */
	
	public String getParameter(String key);
	
	public Map<String,String> getParameterMap();
	
	/**
	 * 解析请求 ：1.解析出url 2.解析出参数 name,age 3解析出请求 get/post/head
	 * 
	 */
	
	public void parse();
	
	public String getServerName();
	
	public int getServerPort();
}
