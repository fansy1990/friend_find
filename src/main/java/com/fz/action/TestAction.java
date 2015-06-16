package com.fz.action;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fz.service.TestService;
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
			HUtils.getJobs();
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
