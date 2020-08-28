package com.yc.ThreadPool;

public class SimpleThread extends Thread {
	private boolean runningFlag; //运行的状态 false
	private Taskable task; //要执行的操作
	private int threadNumber;//线程的编号
	private MyNotify myNotify;//通知接口
	
	//标志runningFlag用于控制线程运行状态
	public boolean isRunning(){
		return this.runningFlag;
	}
	public Taskable getTask() {
		return task;
	}
	public void setTask(Taskable task) {
		this.task = task;
	}



	/**
	 * 设置线程的运行状态
	 * @param flag
	 */
	public synchronized void setRunning(boolean flag){
		this.runningFlag=flag;  //设置当前状态为true,表示当前线程已被占用
		if(flag){
			this.notifyAll();  //唤醒其他线程就绪
		}
	}
	
	public SimpleThread(){
		
	}
	
	/*
	 * 初始化当前线程：提示哪个线程工作
	 */
	public SimpleThread(int threadNumber,MyNotify notify){
		runningFlag=false;
		this.threadNumber=threadNumber;
		System.out.println("Thread"+ threadNumber +"started");
		this.myNotify=notify;
	}
	
	public synchronized void run(){
		try {
			while(true){
				if(!runningFlag){//无限循环
					this.wait();//判断标志位是否为true  如果runningFlag为false  等待调用
				}else{
					System.out.println("线程"+threadNumber+"正在执行任务");
					Object returnValue=this.task.doTask();
					if(myNotify!=null){
						myNotify.notifyResult(returnValue);
					}
					System.out.println("线程"+threadNumber+"任务运行完毕");
					setRunning(false);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupt");
		}
	}

}
