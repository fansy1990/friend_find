/**
 * 
 */
package com.fz.util;

//import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fz.model.ObjectInterface;
import com.fz.service.DBService;

/**
 * 工具类
 * @author fansy
 * @date 2015-6-8
 */
public class Utils {

	// hadoop 常量
//	private static Map<String,String> HADOOPCONSTANTS=new HashMap<String,String>();
	private static ResourceBundle resb = null;
	private static PrintWriter  writer=null;
	
	@Resource
	private static DBService dBService;
	
	public static String getKey(String key,boolean dbOrFile){
		if(dbOrFile){
			return dBService.getHConstValue(key);
		}
		if(resb==null){
			Locale locale = new Locale("zh", "CN"); 
            resb = ResourceBundle.getBundle("util", locale); 
		}
        return resb.getString(key);
	}
	
	/**
	 * 向PrintWriter中输入数据
	 * @param info
	 */
	public static void write2PrintWriter(String info){
		try{
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			writer= ServletActionContext.getResponse().getWriter();
			
			writer.write(info);//响应输出
			//释放资源，关闭流
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param flag
	 */
	public static void write2PrintWriter(boolean flag) {
			write2PrintWriter(String.valueOf(flag));
	}

	/**
	 * 根据类名获得实体类
	 * @param tableName
	 * @param json
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings("unchecked")
	public static Object getEntity(String tableName, String json) throws ClassNotFoundException, InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		Class<?> cl = Class.forName(tableName);
		ObjectInterface o = (ObjectInterface)cl.newInstance();
		Map<String,Object> map = new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			//convert JSON string to Map
			map = mapper.readValue(json, Map.class);
			return o.setObjectByMap(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getEntityPackages(String tableName){
		return "com.fz.model."+tableName;
	}
	
	
	
}
