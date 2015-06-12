/**
 * 
 */
package com.fz.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fz.dao.BaseDAO;
import com.fz.model.HConstants;
import com.fz.model.LoginUser;
import com.fz.model.UserData;

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
