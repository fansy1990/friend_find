/**
 * 
 */
package com.fz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fz.dao.BaseDAO;
import com.fz.model.HConstants;
import com.fz.model.LoginUser;
import com.fz.model.UserData;
import com.fz.util.Utils;

/**
 * 数据库service
 * @author fansy
 * @param <T>
 * @date 2015-6-10
 */
@Service("dBService")

public class DBService {
	
	private Logger log = LoggerFactory.getLogger(DBService.class);
	@Resource
	private BaseDAO<HConstants> baseDaoHConst ;
	@Resource
	private BaseDAO<LoginUser> baseDaoLU;
	@Resource
	private BaseDAO<UserData> baseDaoUD;
	@Resource
	private BaseDAO<Object> baseDao;
	
	/**
	 * 用户登录检查
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean getLoginUser(String username,String password){
		String hql ="from LoginUser lu where lu.username='"+username+"'";
		List<LoginUser> lus = baseDaoLU.find(hql);
		if(lus.size()<1){
			log.info("没有此用户，username：{}",username);
			return false;
		}
		if(lus.size()>1){
			log.info("登录检查多个重名用户，请检查数据库，用户名为：{}",username);
			return false;
		}
		LoginUser lu = lus.get(0);
		if(lu.getPassword().equals(password)){
			log.info("用户：'"+username+"' 登录成功！");
			return true;
		}
		return false;
	}

	
	/**
	 * 分页获取tableName 所有数据
	 * @param tableName:类实体名
	 * @param rows
	 * @param page
	 * @return
	 */
	public Map<String,Object> getTableData(String tableName,int rows,int page){
		String hql = "from "+tableName;
		String hqlCount = "select count(1) from "+tableName;
		List<Object> list =baseDao.find(hql, new Object[]{}, page, rows);
		Map<String ,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("total", baseDaoLU.count(hqlCount));
		jsonMap.put("rows", list);
		return jsonMap;
	}
	
	public boolean deleteById(String tableName,String id){
		String hql ="delete " + tableName +"  tb where tb.id='"+id+"'";
		try{
			Integer ret = baseDao.executeHql(hql);
			log.info("删除表{},删除了{}条记录！",new Object[]{tableName,ret});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateOrSave(String tableName,String json){
		try{
			// 根据表名获得实体类，并赋值
			Object o = Utils.getEntity(Utils.getEntityPackages(tableName),json);
			baseDao.saveOrUpdate(o);
			log.info("保存表{}！",new Object[]{tableName});
		}catch(Exception e){
			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获得Hadoop集群配置
	 * @param key
	 * @return
	 */
	public String getHConstValue(String key){
		
		HConstants hc = baseDaoHConst.find("from HConstants hc where hc.custKey='"+key+"'").get(0);
		if(hc==null){
			log.info("Hadoop基础配置表找不到配置的key：{}",key);
			return null;
		}
		return hc.getCustValue();
	}
	
	
	
	
	public BaseDAO<HConstants> getBaseDaoHConst() {
		return baseDaoHConst;
	}
	public void setBaseDaoHConst(BaseDAO<HConstants> baseDaoHConst) {
		this.baseDaoHConst = baseDaoHConst;
	}
	public BaseDAO<LoginUser> getBaseDaoLU() {
		return baseDaoLU;
	}
	public void setBaseDaoLU(BaseDAO<LoginUser> baseDaoLU) {
		this.baseDaoLU = baseDaoLU;
	}
	public BaseDAO<UserData> getBaseDaoUD() {
		return baseDaoUD;
	}
	public void setBaseDaoUD(BaseDAO<UserData> baseDaoUD) {
		this.baseDaoUD = baseDaoUD;
	}
	
}
