package com.fz.action;

import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.fz.service.TestService;
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
		PrintWriter writer=null;
		String info="name:"+name+",email:"+email;
		System.out.println(info);
		try{
			writer= ServletActionContext.getResponse().getWriter();
			writer.write(info);//响应输出
			testService.saveUser();
			//释放资源，关闭流
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
