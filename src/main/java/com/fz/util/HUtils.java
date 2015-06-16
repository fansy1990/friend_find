/**
 * 
 */
package com.fz.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;

/**
 * Hadoop 工具类
 * @author fansy
 * @date 2015-5-28
 */
public class HUtils {

	private static Configuration conf = null;
	private static FileSystem fs = null;
	
	public static boolean flag = true; // get configuration from db or file  ,true : db,false:file
	
	// 第一个job的启动时间阈值，大于此时间才是要取得的的真正监控任务
	
	private static long jobStartTime=0L;// 使用 System.currentTimeMillis() 获得
	private static JobClient jobClient=null;
	
	public static Configuration getConf(){
		
		if(conf ==null){
			conf = new Configuration ();
			// get configuration from db or file
			conf.setBoolean("mapreduce.app-submission.cross-platform", 
					"true".equals(Utils.getKey("mapreduce.app-submission.cross-platform",flag)));// 配置使用跨平台提交任务  
			conf.set("fs.defaultFS", Utils.getKey("fs.defaultFS",flag));//指定namenode    
			conf.set("mapreduce.framework.name", Utils.getKey("mapreduce.framework.name",flag));  // 指定使用yarn框架  
			conf.set("yarn.resourcemanager.address", Utils.getKey("yarn.resourcemanager.address",flag)); // 指定resourcemanager  
			conf.set("yarn.resourcemanager.scheduler.address", 
					Utils.getKey("yarn.resourcemanager.scheduler.address",flag));// 指定资源分配器
			
		}
		
		return conf;
	}
	
	public static FileSystem getFs(){
		if(fs==null){
			try {
				fs=FileSystem.get(getConf());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fs;
	}
	/**
	 * 获取hdfs文件目录及其子文件夹信息
	 * @param input
	 * @param recursive
	 * @return
	 * @throws IOException
	 */
	public static  String getHdfsFiles(String input,boolean recursive) throws IOException{
		RemoteIterator<LocatedFileStatus> files=getFs().listFiles(new Path(input), recursive);
		StringBuffer buff = new StringBuffer();
		while(files.hasNext()){
			buff.append(files.next().getPath().toString()).append("<br>");
		}
		
		return buff.toString();
	}
	
	/**
	 * 根据时间来判断，然后获得Job的状态，以此来进行监控
	 * @throws IOException 
	 */
	public static void getJobs() throws IOException{
		JobStatus[]jss =getJobClient().getAllJobs();
		List<JobStatus> jsList= new ArrayList<JobStatus>();
		for(JobStatus js: jss){
			if(js.getStartTime()>jobStartTime){
				jsList.add(js);
			}
		}
		System.out.println(jsList);
	}

	public static void printJobStatus(JobStatus js){
		System.out.println(new java.util.Date()+":jobId:"+js.getJobID().toString()+
				",map:"+js.getMapProgress()+",reduce:"+js.getReduceProgress()+",finish:"+js.getRunState());
	}
	/**
	 * @return the jobClient
	 */
	public static JobClient getJobClient() {
		if(jobClient==null){
			try {
				jobClient= new JobClient(getConf());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jobClient;
	}

	/**
	 * @param jobClient the jobClient to set
	 */
	public static void setJobClient(JobClient jobClient) {
		HUtils.jobClient = jobClient;
	}
	
}
