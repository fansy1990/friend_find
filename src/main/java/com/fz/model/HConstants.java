package com.fz.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hconstants")
/**
 * 集群配置表
 * @author fansy
 * @date 2015-6-10
 */
public class HConstants implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String custKey;
	private String custValue;
	private String description;
	
	public HConstants(){	}
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustKey() {
		return custKey;
	}

	public void setCustKey(String custKey) {
		this.custKey = custKey;
	}

	public String getCustValue() {
		return custValue;
	}

	public void setCustValue(String custValue) {
		this.custValue = custValue;
	}
	
}