package com.fz.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="loginuser")
/**
 * 用户登录表User.java
 * @author fansy
 * @date 2015-6-10
 */
public class LoginUser implements Serializable,ObjectInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String password;
	private String description;
	
	public LoginUser(){	}
	public LoginUser(String username,String password){
		this.username=username;
		this.password=password;
	}
	
	public Object setObjectByMap(Map<String,Object> map){
		LoginUser lu = new LoginUser();
		lu.setDescription((String)map.get("description"));
		lu.setId((Integer)map.get("id"));// 修改是有id的，新增没有id
		lu.setPassword((String)map.get("password"));
		lu.setUsername((String)map.get("username"));
		return lu;
	}
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
