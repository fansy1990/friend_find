package com.fz.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fz.service.TestService;
//import com.fz.thread.TestThread;
import com.fz.util.HUtils;
import com.fz.util.Utils;
import com.opensymphony.xwork2.ActionSupport;

@Component("testAction")
public class TestAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private TestService testService;
	private String email;
	private String name;
	
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void test(){	
		try {
			//提交一个Hadoop MR任务的基本流程
			// 1. 设置提交时间阈值
			HUtils.setJobStartTime(System.currentTimeMillis());// 使用当前时间即可
			// 2. 使用Thread的方式启动一组MR任务
//			new Thread(new TestThread()).start();
			// 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
//			HUtils.getJobs();
			Utils.write2PrintWriter("monitor");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化插入数据库表中数据
	 * 更新登录表
	 * 更新集群配置表
	 */
	public void insertTable(){
		try{
			boolean flag = testService.insertTables();
			Utils.write2PrintWriter(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
