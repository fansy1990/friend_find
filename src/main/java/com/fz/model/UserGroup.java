package com.fz.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usergroup")
/**
 * 用户群组表
 * @author fansy
 * @date 2015-7-16
 */
public class UserGroup implements Serializable,ObjectInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Integer groupType;
	
	public UserGroup(){	}
	public UserGroup(Integer userId,Integer group){
		this.setUserId(userId);
		this.setGroupType(group);
	}
	public UserGroup(Integer id,Integer userId,Integer group){
		this.setUserId(userId);
		this.setGroupType(group);
		this.setId(id);
	}
	public Object setObjectByMap(Map<String,Object> map){
		UserGroup lu = new UserGroup();

		lu.setId((Integer)map.get("id"));// 修改是有id的，新增没有id
		lu.setUserId((Integer)map.get("userId"));
		lu.setGroupType((Integer)map.get("group"));
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
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the groupType
	 */
	public Integer getGroupType() {
		return groupType;
	}
	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	
}
