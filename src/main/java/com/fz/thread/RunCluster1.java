/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

import com.fz.fastcluster.DeltaDistanceJob;
import com.fz.fastcluster.LocalDensityJob;
import com.fz.fastcluster.SortJob;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 * 1. 寻找每个向量的局部密度
 * 2. 寻找每个向量的最远距离
 * @author fansy
 * @date 2015-6-16
 */
public class RunCluster1 implements Runnable {

	private String input;
	private String numReducerDensity;
	private String numReducerDistance;
	private String numReducerSort;
	private String dc;
	private String method;
	
	public RunCluster1(String input,String dc,String method,String numReducerDensity,
			String numReducerDistance,String numReducerSort){
		this.input=input;
		this.dc=dc;
		this.method=method;
		this.numReducerDensity=numReducerDensity;
		this.numReducerDistance=numReducerDistance;
		this.numReducerSort=numReducerSort;
	}
	@Override
	public void run() {
		String [] args =new String[]{
				HUtils.getHDFSPath(input),
				HUtils.getHDFSPath(HUtils.LOCALDENSITYOUTPUT),
				dc,
				method,
				numReducerDensity
		};
		try {
			int ret=
			ToolRunner.run(HUtils.getConf(), new LocalDensityJob(),args );
			if(ret!=0){
				Utils.simpleLog("LocalDensityJob任务运行失败！");
				return ;
			}
			Thread.sleep(3000);// 等待3秒时间
			args=new String[]{
					HUtils.getHDFSPath(input),// 使用距离计算后的路径作为输入
					HUtils.getHDFSPath(HUtils.DELTADISTANCEOUTPUT),
					HUtils.getHDFSPath(HUtils.LOCALDENSITYOUTPUT),
					numReducerDistance
			};
			
			ret=ToolRunner.run(HUtils.getConf(), new DeltaDistanceJob(), args);
			if(ret!=0){
				Utils.simpleLog("DeltaDistanceJob任务运行失败！");
				return ;
			}
			Thread.sleep(3000);// 等待3秒时间
			
			args=new String[]{
					HUtils.getHDFSPath(HUtils.DELTADISTANCEOUTPUT),
					HUtils.getHDFSPath(HUtils.SORTOUTPUT),
					numReducerSort
			};
			ret=ToolRunner.run(HUtils.getConf(), new SortJob(), args);
			if(ret!=0){
				Utils.simpleLog("SortJob任务运行失败！");
				return ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}

	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
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
