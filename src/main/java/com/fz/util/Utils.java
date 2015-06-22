/**
 * 
 */
package com.fz.util;

//import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.context.ContextLoader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fz.model.ObjectInterface;
import com.fz.service.DBService;

/**
 * 工具类
 * @author fansy
 * @date 2015-6-8
 */
public class Utils {

	// hadoop 常量
	
	//
	public static String baseServicePacakges="com.fz.service.*";
	
//	private static Map<String,String> HADOOPCONSTANTS=new HashMap<String,String>();
	private static ResourceBundle resb = null;
	private static PrintWriter  writer=null;
	
	
	private static String[] userdata_attributes=new String[]{"Id","Reputation","CreationDate","DisplayName",
			"EmailHash","LastAccessDate","Location","Age","AboutMe","Views","UpVotes","DownVotes"};
	private static String userdata_elementName="row";
	private static String userdata_xmlPath="ask_ubuntu_users.xml";
	
	private static int counter=0;// 在任务运行时返回递增的点字符串
	
	/**
	 * 初始化登录表数据
	 */
//	static{// 这种方式不行
//		dBService.insertLoginUser();
//		System.out.println(new java.util.Date()+"：初始化登录表完成！");
//	}
	
	public static String getKey(String key,boolean dbOrFile){
		if(dbOrFile){
			DBService dbService =(DBService)SpringUtil.getBean("dBService");
			return dbService.getHConstValue(key);
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
	/**
	 *  获得表的实体类的全路径
	 * @param tableName
	 * @return
	 */
	public static String getEntityPackages(String tableName){
		return "com.fz.model."+tableName;
	}
	/**
	 * 获得根路径
	 * @return
	 */
	private static String getRootPath(){
		return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
	}
	
	/**
	 * 获得根路径下面的subPath路径
	 * @param subPath
	 * @return
	 */
	public static String getRootPathBasedPath(String subPath){
		return getRootPath()+subPath;
	}
	
	/**
	 * 把xml转为字符串数组
	 * @param xmlPath
	 * @return
	 */
	public static List<String[]> parseXml2StrArr(String xmlPath){
		if(xmlPath==null){
			
			String tmp= getRootPath();
			System.out.println(tmp);
			xmlPath=tmp+"WEB-INF/classes/"+userdata_xmlPath;
		}
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		Document doc=null;
		  try{
		  DocumentBuilder db=dbf.newDocumentBuilder();  
		  doc=db.parse(new File(xmlPath)); 
		  	}catch(Exception e){
			  e.printStackTrace();
		  }
		  List<String[]> strings=new ArrayList<String[]>();
        try{
            NodeList nodeList=doc.getElementsByTagName(userdata_elementName);
            
            for(int i=0;i< nodeList.getLength();i++){
	            Node node0=nodeList.item(i);
	            NamedNodeMap m1=node0.getAttributes();
	            String[] attrValues=new String[userdata_attributes.length];
	            for(int j=0;j<userdata_attributes.length;j++){
	            	Node nodeAttri=m1.getNamedItem(userdata_attributes[j]);
	            	if(nodeAttri!=null){
	            		attrValues[j]=nodeAttri.getNodeValue();
	            	}
	            }
	           strings.add(attrValues); // throw data format exception
            }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return strings;
	}

	/**
	 * 获得递增点字符串
	 * @return
	 */
	public static String getDotState(String pre){
		counter++;
		StringBuffer buff =new StringBuffer();
		for(int i=0;i<counter;i++){
			buff.append(".");
		}
		if(counter>=7){
			counter=0;
		}
		return pre+buff.toString();
	}
	
	
	
}
