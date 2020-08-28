package com.yc.javax.servlet.http;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import com.yc.javax.servlet.ServletResponse;

public class JspWriter extends PrintWriter {
	private ServletResponse response;
	
	public JspWriter(OutputStream out) {
		super(out);
	}

	public JspWriter(OutputStream out, ServletResponse response) {
		super(out);
		this.response = response;
	}
	
	public void println(String content){
		//协议的拼接
		long length=content.getBytes().length;
		StringBuffer sb=new StringBuffer();
		
		String contentType="text/html";
		String protocal="HTTP/1.0 200 OK\r\nContent-type: "+contentType+"\r\nContent-Length: "+
		length+"\r\n";
		
		sb.append(protocal);
		
		//拼接Cookie
		Cookie[] cs=((HttpServletResponse)this.response).getCookies();
		if(cs!=null && cs.length>0){
			sb.append("Set-Cookies: ");
			for(Cookie c:cs){
				sb.append(c.toString());
			}
		}
		sb.append("\r\n\r\n");
		sb.append(content);
		super.println(sb.toString());
		super.flush();
	}
	

}
