/**
 * 
 */
package com.fz.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;

/**
 * Hadoop 工具类
 * @author fansy
 * @date 2015-5-28
 */
public class HUtils {

	private static Configuration conf = null;
	
	public static Configuration getConf(){
		
		if(conf ==null){
			conf = new Configuration ();
			conf.setBoolean("mapreduce.app-submission.cross-platform", true);// 配置使用跨平台提交任务  
		    conf.set("fs.defaultFS", "hdfs://node101:8020");//指定namenode    
		    conf.set("mapreduce.framework.name", "yarn");  // 指定使用yarn框架  
		    conf.set("yarn.resourcemanager.address", "node101:8032"); // 指定resourcemanager  
		    conf.set("yarn.resourcemanager.scheduler.address", "node101:8030");// 指定资源分配器
		}
		
		return conf;
	}
	
	
	/**
	 * 使用欧式距离
	 * @param inputI
	 * @param ds
	 * @return
	 */
	public static double getDistance(double[] inputI, double[] ds) {
		double error =0.0;
		for(int i=0;i<inputI.length;i++){
			error+=(inputI[i]-ds[i])*(inputI[i]-ds[i]);
		}
		return Math.sqrt(error);
	}
	
	

	/**
	 * @param value
	 * @return
	 */
	public static double[] getInputI(Text value,String splitter) {
		return getInputI(value.toString(),splitter);
	}
	
	public static double[] getInputI(String value,String splitter){
		String[] inputStrArr = value.split(splitter);
		double[] inputI = new double[inputStrArr.length];
		
		for(int i=0;i<inputI.length;i++){
			inputI[i]= Double.parseDouble(inputStrArr[i]);
		}
		return inputI;
	}
}
