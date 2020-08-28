package com.yc.ThreadPool;

import java.util.Vector;

public class ThreadPoolManger {
	private int initThreads;//线程池中初始线程的数量
	private Vector vector;//Vector类可以实现可增长的对象数组  ，用于存多个线程
	private MyNotify notify;
	
	public void setInitThreads(int initThreads){
		this.initThreads=initThreads;
	}
	
	public ThreadPoolManger(){
		
	}
	
	public ThreadPoolManger(int initThreads,MyNotify notify){
		this.notify=notify;
		this.setInitThreads(initThreads);
		System.out.println("线程池开始运行了");
		vector =new Vector();
		for(int i=1;i<=initThreads;i++){
			SimpleThread thread=new SimpleThread(i,this.notify);
			vector.addElement(thread);//将指定的组件添加到此向量的末尾  将其大小加1
			thread.setDaemon(true);
			thread.start();
		}
		/**
		 * TODO:启动一个后天线程  根据空闲(20%)的比例 来收缩线程池
		 *initThreads:5
		 *当前线程 :50
		 *发现45个空闲的
		 */
	}
	
	public void process(Taskable task){
		int i;
		//TODO:线程选的方法可以改，随机选，按模来选
		for(i=0;i<vector.size();i++){
			SimpleThread currentThread =(SimpleThread) vector.elementAt(i);//返回指定索引处的组件
			if(!currentThread.isRunning()){
				System.out.println("Thread"+(i+1)+"开始执行任务");
				currentThread.setTask(task);
				currentThread.setRunning(true);
				return;
			}
		}
		System.out.println("======================");
		System.out.println("线程池中没有空闲的线程,扩容");
		System.out.println("=======================");
		//TODO:
		//1.判断当前的虚拟机是否还有空闲的内存，如果有  我才开
		//Runtime类 看内存是否够
		//2.slot ->jvm进程
		if(i>=vector.size()){
			int temp=vector.size();
			for(int j=temp+1;j<=temp+10;j++){
				SimpleThread thread=new SimpleThread(j,this.notify);
				vector.addElement(thread);
				thread.start();
			}
			/**
			 * 创建完成后需要重新执行process
			 */
			this.process(task);
		}
	}
	
}
