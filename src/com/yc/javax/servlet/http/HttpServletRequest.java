package com.yc.javax.servlet.http;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.ServletRequest;

public interface HttpServletRequest extends ServletRequest {

	public String getMethod();
	
	public String getRequestURI();
	
	/**
	 * 新增一个获取application的方法
	 */
	public ServletContext getServletContext();
}
