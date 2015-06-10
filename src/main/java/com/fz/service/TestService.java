/**
 * 
 */
package com.fz.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fz.dao.BaseDAO;
import com.fz.model.User;

/**
 * @author fansy
 * @date 2015-2-9
 */
@Service("testService")
public class TestService {
	private Logger log = LoggerFactory.getLogger(TestService.class);
	@Resource
	private BaseDAO<User> baseDao;
	/**
	 * 保存测试完成
	 */
	public int saveUser(){
		User u = new User();
		u.setGender("男");
		u.setUserId("123456");
		int num =(Integer)baseDao.save(u);
//		System.out.println("条数："+num);
		log.info("条数："+num);
		return num;
	}
	/**
	 * 更新测试完成
	 */
	public void updateUser(){
		
	}
	
}
