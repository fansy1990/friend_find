package com.fz.action;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fz.service.DBService;
import com.fz.util.Utils;
import com.opensymphony.xwork2.ActionSupport;

@Component("dBAction")
public class DBAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(DBAction.class);
	@Resource
	private DBService dBService;
	private int rows;
	private int page;
	private String tableName;
	
	private String id;
	
	private String json;
	
	/**
	 * 根据tableName分页获取表数据
	 */
	public void getTableData(){
		Map<String,Object> list = dBService.getTableData(tableName,rows, page);
		String json =JSON.toJSONString(list);
		log.info(json);
		Utils.write2PrintWriter(json);
	}
	
	/**
	 * 安装id删除表中数据
	 */
	public void deleteById(){
		boolean delSuccess =dBService.deleteById(tableName, id);
		String msg="fail";
		if(delSuccess){
			msg="success";
		}
		log.info("删除表"+tableName+(delSuccess?"成功":"失败"+"!"));
		Utils.write2PrintWriter(msg);
	}
	
	/**
	 * 更新或者保存数据
	 */
	public void updateOrSave(){
		boolean delSuccess =dBService.updateOrSave(tableName, json);
		String msg="fail";
		if(delSuccess){
			msg="success";
		}
		log.info("保存表"+tableName+(delSuccess?"成功":"失败"+"!"));
		Utils.write2PrintWriter(msg);
	}

	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}


	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}


	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}


	
}
