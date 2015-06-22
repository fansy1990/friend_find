/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

import com.fz.fast_cluster.DeltaDistanceJob;
import com.fz.fast_cluster.LocalDensityJob;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-16
 */
public class RunCluster1 implements Runnable {

	private String input;
	private String splitter;
	private String dc;
	private String method;
	
	public RunCluster1(String input,String splitter,String dc,String method){
		this.input=input;
		this.splitter=splitter;
		this.dc=dc;
		this.method=method;
	}
	@Override
	public void run() {
		String [] args ={
				input,
				HUtils.getHDFSPath(HUtils.LOCALDENSITYOUTPUT),
				dc,
				splitter,
				method
		};
		try {
			ToolRunner.run(HUtils.getConf(), new LocalDensityJob(),args );
			String[] ar={
					HUtils.getHDFSPath(HUtils.LOCALDENSITYOUTPUT)+"/part-r-00000",
					HUtils.getHDFSPath(HUtils.DELTADISTANCEOUTPUT)
			};
			ToolRunner.run(HUtils.getConf(), new DeltaDistanceJob(), ar);
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
	public String getSplitter() {
		return splitter;
	}
	public void setSplitter(String splitter) {
		this.splitter = splitter;
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

}
