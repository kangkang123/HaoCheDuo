package cn.longchou.wholesale.utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ============================================================
 * 
 * 版权 ：安博教育集团 版权所有 (c) 2013
 * 
 * 作者:mwqi
 * 
 * 版本 ：1.0
 * 
 * 创建日期 ： 2013-1-18 上午1:05:30
 * 
 * 描述 ：
 * 
 *      线程管理器
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class ThreadPoolManager {
	private ExecutorService service;
	
	private ThreadPoolManager(){
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num);
	}
	
	private static final ThreadPoolManager manager= new ThreadPoolManager();
	
	public static ThreadPoolManager getInstance(){
		return manager;
	}
	
	public void addTask(Runnable runnable){
		service.execute(runnable);
	}
}
