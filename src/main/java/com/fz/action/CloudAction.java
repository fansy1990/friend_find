/**
 * 
 */
package com.fz.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fz.model.CurrentJobInfo;
import com.fz.model.UserData;
import com.fz.service.DBService;
import com.fz.thread.CalDistance;
import com.fz.thread.Deduplicate;
import com.fz.thread.RunCluster1;
import com.fz.thread.RunCluster2;
import com.fz.util.DrawPic;
import com.fz.util.HUtils;
import com.fz.util.Utils;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author fansy
 * @date 2015-6-16
 */
@Component("cloudAction")
public class CloudAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String input;
	private String numReducerDensity;
	private String numReducerDistance;
	private String numReducerSort;
	private String delta;
	private String method;
	
	private String output;
	
	private String record;
	
	
	@Resource
	private DBService dBService;
	
	/**
	 * 提交fast cluster第一步MR任务
	 */
	public void runCluster1(){	
		Map<String ,Object> map = new HashMap<String,Object>();
		try {
			//提交一个Hadoop MR任务的基本流程
			// 1. 设置提交时间阈值,并设置这组job的个数
			//使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);// 
			HUtils.JOBNUM=3;
			// 2. 使用Thread的方式启动一组MR任务
			new Thread(new RunCluster1(input, delta, method,numReducerDensity,
					numReducerDistance,numReducerSort)).start();
			// 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
			
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
	}
	
	/**
	 * 去重任务提交
	 */
	public void deduplicate(){
		Map<String ,Object> map = new HashMap<String,Object>();
		try{
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);
			HUtils.JOBNUM=1;
			new Thread(new Deduplicate(input,output)).start();
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
	}
	
	/**
	 * 画决策图
	 */
	public void drawDecisionChart(){
		String url =HUtils.getHDFSPath(HUtils.SORTOUTPUT);
		String file =Utils.getRootPathBasedPath("pictures\\decision_chart.png");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("path", file);
		try{
			DrawPic.drawPic(url, file);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return ;
		}
		map.put("flag", "true");
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	/**
	 * 根据给定的阈值寻找聚类中心向量，并写入hdfs
	 * 非MR任务，不需要监控，注意返回值
	 */
	public void center2hdfs(){
		// localfile:method
		// 1. 读取SortJob的输出，获取前面k条记录中的大于局部密度和最小距离阈值的id；
		// 2. 根据id，找到每个id对应的记录；
		// 3. 把记录转为double[] ；
		// 4. 把向量写入hdfs
		// 5. 把向量写入本地文件中，方便后面的查看
		Map<String,Object> retMap=new HashMap<String,Object>();
		
		Map<Object,Object> firstK =null;
		List<Integer> ids= null;
		List<UserData> users=null;
		try{
		firstK=HUtils.readSeq(input==null?HUtils.SORTOUTPUT+"/part-r-00000":input,
				100);// 这里默认使用	前100条记录
		ids=HUtils.getCentIds(firstK,numReducerDensity,numReducerDistance);
		// 2
		users = dBService.getTableData("UserData",ids);
		Utils.simpleLog("聚类中心向量有"+users.size()+"个！");
		// 3,4,5
		HUtils.writecenter2hdfs(users,method,output);	
		}catch(Exception e){
			e.printStackTrace();
			retMap.put("flag", "false");
			retMap.put("msg", e.getMessage());
			Utils.write2PrintWriter(JSON.toJSONString(retMap));
			return ;
		}
		retMap.put("flag", "true");
		Utils.write2PrintWriter(JSON.toJSONString(retMap));
		return ;
	}
	
	/**
	 * 快速聚类--执行聚类
	 * 
	 */
	public void runCluster2(){
		Map<String ,Object> map = new HashMap<String,Object>();
		try {
			//提交一个Hadoop MR任务的基本流程
			// 1. 设置提交时间阈值,并设置这组job的个数
			//使用当前时间即可,当前时间往前10s，以防服务器和云平台时间相差
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);// 
			// 由于不知道循环多少次完成，所以这里设置为2，每次循环都递增1
			// 当所有循环完成的时候，就该值减去2即可停止监控部分的循环
			HUtils.JOBNUM=2;
			
			// 2. 使用Thread的方式启动一组MR任务
			new Thread(new RunCluster2(input, output,delta, record)).start();
			// 3. 启动成功后，直接返回到监控，同时监控定时向后台获取数据，并在前台展示；
			
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
	}
	/**
	 * 云平台已经分类好的数据解析并存入数据库中
	 */
	public void group2db(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("monitor", "false");
		try{
			// 解析
			List<Object> list = HUtils.resolve(input);
			// 清库，并入库
			dBService.deleteTable("UserGroup");
			dBService.saveTableData(list);
			map.put("flag", "true");
		}catch(Exception e){
			map.put("flag", "false");
			e.printStackTrace();
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
		
	}
	/**
	 * 解析本地聚类中心数据，并获得数据库中分类数据占比情况
	 * 返回前台显示
	 */
	public void groupcheck(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> centerVec=null;
		List<String> percentVec=null;
		try{
			input = input==null ? HUtils.LOCALCENTERFILE:input;
			centerVec= Utils.getLines(input);
			percentVec=  dBService.getPercent(centerVec.size());
			// 整合数据
			StringBuffer buff = new StringBuffer();
			buff.append("<br>");
			for(int i=0;i<centerVec.size();i++){
				buff.append("聚类中心："+centerVec.get(i)+"\t,占比："+percentVec.get(i)+"<br>");
			}
			map.put("html", buff.toString());
			map.put("flag","true");
		}catch(Exception e){
			map.put("flag", "false");
			map.put("msg", "解析聚类中心出错！");
			e.printStackTrace();
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	
	
	
	/**
	 * 数据库数据解析到云平台,为序列文件，是聚类运行的输入文件
	 * 
	 * [IntWritable,DoubleArrIntWritable]
	 */
	public void db2hdfs(){
		List<Object> list = dBService.getTableAllData("UserData");
		Map<String,Object> map = new HashMap<String,Object>();
		if(list.size()==0){
			map.put("flag", "false");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return ;
		}
		try{
			HUtils.db2hdfs(list,output,Integer.parseInt(record));
		}catch(Exception e){
			map.put("flag", "false");
			map.put("msg", e.getMessage());
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return ;
		}
		map.put("flag", "true");
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	/**
	 * 遍历向量距离文件，寻找最佳阈值
	 */
	public void findbestdc(){
		double dc=0.0;
		Map<String,Object> map = new HashMap<String,Object>();
		int recordInt = Integer.parseInt(record);
		if(HUtils.INPUT_RECORDS==0&&recordInt!=0){
			HUtils.INPUT_RECORDS=recordInt;
		}
		try{
			if(HUtils.INPUT_RECORDS==0){
				map.put("flag", "false");
				map.put("msg", "请先运行计算距离MR任务，或者设置任务运行后的记录数!");
				Utils.write2PrintWriter(JSON.toJSONString(map));
				return ;
			}
			dc=HUtils.findInitDC(Double.parseDouble(delta.substring(0, delta.length()-1))/100, input, 
					HUtils.INPUT_RECORDS);
		}catch(Exception e){
			e.printStackTrace();
			map.put("flag", "false");
			map.put("msg", e.getMessage());
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return ;
		}
		map.put("flag", "true");
		map.put("dc", dc);
		Utils.simpleLog(JSON.toJSONString(map));
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}
	
	/**
	 * 计算向量之间的距离
	 */
	public void caldistance(){
		Map<String ,Object> map = new HashMap<String,Object>();
		try{
			HUtils.setJobStartTime(System.currentTimeMillis()-10000);
			HUtils.JOBNUM=1;
			new Thread(new CalDistance(input,output)).start();
			map.put("flag", "true");
			map.put("monitor", "true");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("monitor", "false");
			map.put("msg", e.getMessage());
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
	}
	
	/**
	 * 监控
	 * @throws IOException
	 */
	public void monitor() throws IOException{
    	Map<String ,Object> jsonMap = new HashMap<String,Object>();
    	List<CurrentJobInfo> currJobList =null;
    	try{
    		currJobList= HUtils.getJobs();
    		jsonMap.put("rows", currJobList);// 放入数据
    		jsonMap.put("total", HUtils.JOBNUM);
    		// 任务完成的标识是获取的任务个数必须等于jobNum，同时最后一个job完成
    		// true 所有任务完成
    		// false 任务正在运行
    		// error 某一个任务运行失败，则不再监控
    		if(currJobList.size()==0){
    			jsonMap.put("finished", "false");
//    			return ;
    		}
    		if(currJobList.size()>=HUtils.JOBNUM){// 如果返回的list有JOBNUM个，那么才可能完成任务
    			if("success".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "true");
    				// 运行完成，初始化时间点
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else if("running".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "false");
    			}else{// fail 或者kill则设置为error
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}
    		}else if(currJobList.size()>0){
    			if("fail".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))||
    					"kill".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else{
    				jsonMap.put("finished", "false");
    			}
        	}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		jsonMap.put("finished", "error");
    		HUtils.setJobStartTime(System.currentTimeMillis());
    	}
    	HUtils.checkJsonMap(jsonMap);// 如果jsonMap.get("rows").size()!=HUtils.JOBNUM,则添加
    	System.out.println(new java.util.Date()+":"+JSON.toJSONString(jsonMap));
    	
    	Utils.write2PrintWriter(JSON.toJSONString(jsonMap));// 使用JSON数据传输
    	
    	return ;
    }

	/**
	 * 单个任务监控
	 * @throws IOException
	 */
	public void monitorone() throws IOException{
    	Map<String ,Object> jsonMap = new HashMap<String,Object>();
    	List<CurrentJobInfo> currJobList =null;
    	try{
    		currJobList= HUtils.getJobs();
//    		jsonMap.put("rows", currJobList);// 放入数据
    		jsonMap.put("jobnums", HUtils.JOBNUM);
    		// 任务完成的标识是获取的任务个数必须等于jobNum，同时最后一个job完成
    		// true 所有任务完成
    		// false 任务正在运行
    		// error 某一个任务运行失败，则不再监控
    		
    		if(currJobList.size()>=HUtils.JOBNUM){// 如果返回的list有JOBNUM个，那么才可能完成任务
    			if("success".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "true");
    				// 运行完成，初始化时间点
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else if("running".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "false");
    			}else{// fail 或者kill则设置为error
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}
    		}else if(currJobList.size()>0){
    			if("fail".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))||
    					"kill".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "error");
    				HUtils.setJobStartTime(System.currentTimeMillis());
    			}else{
    				jsonMap.put("finished", "false");
    			}
        	}
    		
    		
    		if(currJobList.size()==0){
    			jsonMap.put("finished", "false");
//    			return ;
    		}else{
    			if(jsonMap.get("finished").equals("error")){
    				CurrentJobInfo cj =currJobList.get(currJobList.size()-1);
    				cj.setRunState("Error!");
    				jsonMap.put("rows", cj);
    			}else{
    				jsonMap.put("rows", currJobList.get(currJobList.size()-1));
    			}
    		}
    		jsonMap.put("currjob", currJobList.size());
    	}catch(Exception e){
    		e.printStackTrace();
    		jsonMap.put("finished", "error");
    		HUtils.setJobStartTime(System.currentTimeMillis());
    	}
    	System.out.println(new java.util.Date()+":"+JSON.toJSONString(jsonMap));
    	
    	Utils.write2PrintWriter(JSON.toJSONString(jsonMap));// 使用JSON数据传输
    	
    	return ;
    }

	
	
	/**
	 * 上传文件
	 */
	public void upload(){
		Map<String,Object> map = HUtils.upload(input, HUtils.getHDFSPath(HUtils.SOURCEFILE));
		
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	/**
	 * 下载文件到本地文件夹
	 */
	public void download(){
		// output 应该和HUtils.DEDUPLICATE_LOCAL保持一致
		
		Map<String,Object> map = HUtils.downLoad(input, Utils.getRootPathBasedPath(output));
		
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	/**
	 * 解析入库
	 */
	public void resolve2db(){
		Map<String,Object> map=dBService.insertUserData(Utils.getRootPathBasedPath(input));
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return ;
	}
	

	public String getInput() {
		return input;
	}


	public void setInput(String input) {
		this.input = input;
	}


	public String getDelta() {
		return delta;
	}


	public void setDelta(String delta) {
		this.delta = delta;
	}



	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the dBService
	 */
	public DBService getdBService() {
		return dBService;
	}

	/**
	 * @param dBService the dBService to set
	 */
	public void setdBService(DBService dBService) {
		this.dBService = dBService;
	}

	/**
	 * @return the record
	 */
	public String getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(String record) {
		this.record = record;
	}

	public String getNumReducerDensity() {
		return numReducerDensity;
	}

	public void setNumReducerDensity(String numReducerDensity) {
		this.numReducerDensity = numReducerDensity;
	}

	public String getNumReducerDistance() {
		return numReducerDistance;
	}

	public void setNumReducerDistance(String numReducerDistance) {
		this.numReducerDistance = numReducerDistance;
	}

	/**
	 * @return the numReducerSort
	 */
	public String getNumReducerSort() {
		return numReducerSort;
	}

	/**
	 * @param numReducerSort the numReducerSort to set
	 */
	public void setNumReducerSort(String numReducerSort) {
		this.numReducerSort = numReducerSort;
	}


}
