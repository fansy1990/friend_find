/**
 * 
 */
package com.fz.service;

import java.util.ArrayList;
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
//	@Resource
//	private BaseDAO<HConstants> baseDaoHConst ;
//	@Resource
//	private BaseDAO<LoginUser> baseDaoLU;
//	@Resource
//	private BaseDAO<UserData> baseDaoUD;
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
		List<Object> lus = baseDao.find(hql);
		if(lus.size()<1){
			log.info("没有此用户，username：{}",username);
			return false;
		}
		if(lus.size()>1){
			log.info("登录检查多个重名用户，请检查数据库，用户名为：{}",username);
			return false;
		}
		LoginUser lu = (LoginUser) lus.get(0);
		if(lu.getPassword().equals(password)){
			log.info("用户：'"+username+"' 登录成功！");
			return true;
		}
		return false;
	}
/**
 * 测试表中是否有数据 
 * @param tableName
 * @return
 */
	public boolean getTableData(String tableName){
		String hql ="from "+tableName+" ";
		List<Object> td = baseDao.find(hql);
		if(td.size()>0){
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
		jsonMap.put("total", baseDao.count(hqlCount));
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
	
	/**
	 * 更新或者插入表
	 * @param tableName
	 * @param json
	 * @return
	 */
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
		
		HConstants hc = (HConstants) baseDao.find("from HConstants hc where hc.custKey='"+key+"'").get(0);
		if(hc==null){
			log.info("Hadoop基础配置表找不到配置的key：{}",key);
			return null;
		}
		return hc.getCustValue();
	}
	
	/**
	 * 初始化登录表
	 * @param tableName
	 * @return
	 */
	public boolean insertLoginUser(){
		try {
			baseDao.executeHql("delete LoginUser"); 
			baseDao.save(new LoginUser("admin","admin"));
			baseDao.save(new LoginUser("test","test"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 初始化HConstants
	 * @return
	 */
	public boolean insertHConstants(){
		try{
			baseDao.executeHql("delete HConstants");
			baseDao.save(new HConstants("mapreduce.app-submission.cross-platform","true","是否跨平台提交任务"));
			baseDao.save(new HConstants("fs.defaultFS","hdfs://node101:8020","namenode主机及端口"));
			baseDao.save(new HConstants("mapreduce.framework.name","yarn","mapreduce 使用配置"));
			baseDao.save(new HConstants("yarn.resourcemanager.address","node101:8032","ResourceManager主机及端口"));
			baseDao.save(new HConstants("yarn.resourcemanager.scheduler.address","node101:8030","Scheduler主机及端口"));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 初始化UserData
	 * 此处使用批量插入
	 * 直接插入，后期可以考虑使用jdbc的批量插入
	 * @return
	 */
	public boolean insertUserData(){
		try{
			baseDao.executeHql("delete UserData");
			List<String[]> strings= Utils.parseXml2StrArr(null);
			List<Object> uds = new ArrayList<Object>();
			for(String[] s:strings){
				uds.add(new UserData(s));
			}
			int ret =baseDao.saveBatch(uds);
			log.info("用户表批量插入了{}条记录!",ret);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertUserData_b(){
		try{
			baseDao.executeHql("delete UserData");
			List<String[]> strings= Utils.parseXml2StrArr(null);
//			List<Object> uds = new ArrayList<Object>();
			int ret=0;
//			for(String[] s:strings){
//				baseDao.save(new UserData(s));
//				ret++;
//				if(ret%1000==0){
//					log.info("用户表批量插入了{}条记录...",ret);
//				}
//			}
			baseDao.save(new UserData(strings.get(0)));
//			int ret =baseDao.saveBatch(uds);
			log.info("用户表批量插入了{}条记录!",ret);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
//	public BaseDAO<HConstants> getBaseDaoHConst() {
//		return baseDaoHConst;
//	}
//	public void setBaseDaoHConst(BaseDAO<HConstants> baseDaoHConst) {
//		this.baseDaoHConst = baseDaoHConst;
//	}
//	public BaseDAO<LoginUser> getBaseDaoLU() {
//		return baseDaoLU;
//	}
//	public void setBaseDaoLU(BaseDAO<LoginUser> baseDaoLU) {
//		this.baseDaoLU = baseDaoLU;
//	}
//	public BaseDAO<UserData> getBaseDaoUD() {
//		return baseDaoUD;
//	}
//	public void setBaseDaoUD(BaseDAO<UserData> baseDaoUD) {
//		this.baseDaoUD = baseDaoUD;
//	}
	
}
