package com.fz.model;

import java.io.Serializable;
import java.util.Map;

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
public class HConstants implements Serializable,ObjectInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String custKey;
	private String custValue;
	private String description;
	
	public HConstants(){	}
	
	public HConstants(String key,String value,String desc){
		this.custKey=key;
		this.custValue=value;
		this.description=desc;
	}
	
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

	@Override
	public Object setObjectByMap(Map<String, Object> map) {
		HConstants hc = new HConstants();
		hc.setCustKey((String)map.get("custKey"));
		hc.setCustValue((String)map.get("custValue"));
		hc.setDescription((String)map.get("description"));
		hc.setId((Integer)map.get("id"));
		return hc;
	}
	
}
