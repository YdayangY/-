版 本一:   http服务器，提供静态资源访问.        

     请求部分:   
          3)图片
          	GET /xxx/xxx.jpg HTTP/1.1
			Referer: http://localhost:8080/wowotuanStatic/index.html
			Sec-Fetch-Dest: image
			User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.16
    
    响应部分:
          3)图片:
           HTTP/1.1 200 OK
           Accept-Ranges: bytes
			Content-Length: 92174      **
			Content-Type: image/jpeg   **
			Date: Sat, 11 Apr 2020 02:22:41 GMT
			ETag: W/"92174-1586571146000"
			Last-Modified: Sat, 11 Apr 2020 02:12:26 GMT
			Server: Apache-Coyote/1.1
			
			响应实体
					
			
服务器功能:
1. 接收客户端的请求解析出它请求的文件名及相对路径. 
2. 查找这个文件是否存在，  不存在-> 404页面
          存在 ->  
                1）读取这个资源
                2) 构建响应协议     Content-Type:  浏览器根据响应中的  Content-Type来决定使用什么引擎来解析数据
                          text/html:  html  -> html渲染
                          css   :    css引擎
                          js:     js引擎
                          图片:   图片引擎. 
                          
                        Content-Length                      

用到的技术:
			1. ServerSocket  ->  Socket  
			2. 多线程
			3. log4j
			4. dom解析


KittyServer:
	xml的解析端口; 
   ServerSocket ss=new ServerSocket(  端口) ;

    Socket s=ss.accept();
    Thread t=new Thread(  new 任务(  s )  );
    t.start();
    
    
注意的问题:
  1. HttpServletRequest类中的  private String readFromInputStream()方法，要一次读取所有的请求头数据. 