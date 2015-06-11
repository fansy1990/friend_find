package com.fz.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fz.service.DBService;
import com.fz.util.Utils;
import com.opensymphony.xwork2.ActionSupport;

@Component("loginUserAction")
public class LoginUserAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(LoginUserAction.class);
	@Resource
	private DBService dBService;
	
	
	private String password;
	private String username;
	
	
	public void login(){
		log.info("User:{} 正在登陆系统...",username);
		try{
			boolean flag = dBService.getLoginUser(username, password);
			Utils.write2PrintWriter(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	/**
	 * @return the dBService
	 */
	public DBService getdBService() {
		return dBService;
	}
	/**
	 * @param dBService the dBService to set
	 */
	public void setdBService(DBService dBService) {
		this.dBService = dBService;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
