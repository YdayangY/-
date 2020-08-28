package com.yc.ThreadPool;
//通知接口  用于通知主线程  当前线程中的执行任务情况
public interface MyNotify {
	public void notifyResult(Object result);//用Object为了更好的扩展
}
