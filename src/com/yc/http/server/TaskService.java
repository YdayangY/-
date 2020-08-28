package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.yc.ThreadPool.Taskable;

public class TaskService implements Taskable{
	private Socket socket;
	private InputStream iis;
	private OutputStream oos;
	private boolean flag;
	public TaskService(Socket socket){
		this.socket=socket;
		try {
			this.iis=this.socket.getInputStream();
			this.oos=this.socket.getOutputStream();
			flag=true;
		} catch (IOException e) {
			YcConstants.logger.error("failed to get stream");
			flag=false;
			throw new RuntimeException(e);
		}
	}
	
	
	@Override  //HTTP协议是一次请求和响应  http是无状态的
	public Object doTask() {

		if(flag){
			//包装一个HttpServletRequest对象  功能是从 iis中取数据 解析请求信息  保存信息
			YcHttpServletRequest request=new YcHttpServletRequest(this.iis);//uri
			//包装一个HttpServletResponse对象: 从request中取信息(文件的资源)，读取资源 构建响应头  回给客户端
			YcHttpServletResponse response=new YcHttpServletResponse(request, this.oos);
			//response.sendRedirect();
			Processor processor=null;
			if(request.getRequestURI().endsWith(".do")){
				processor=new DynamicProcessor();
			}else{
				processor=new StaticProcessor();
			}
			processor.process(request, response);
			
		}
		try {
			this.socket.close();//http协议是一个基于请求/响应模式的无状态
		} catch (IOException e) {
			YcConstants.logger.error("failed to close connection to client", e);
		}
		return flag;
	}

}
