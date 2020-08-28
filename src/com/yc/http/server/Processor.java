package com.yc.http.server;

import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

/**
 * 资源处理器  ： 处理静态或动态资源
 * @author 聂仁美男朋友
 *
 */
public interface Processor {
	/**
	 * 处理资源(动、静)方法
	 * 
	 */
	public void process(ServletRequest request,ServletResponse response);
}
